package com.example.parkingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkingapp.ui.theme.ParkingAppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ParkingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkingAppTheme {
                ParkingAppScreen(viewModel)
            }
        }
    }
}

@Composable
fun ParkingAppScreen(viewModel: ParkingViewModel) {
    var selectedLot by remember { mutableStateOf<Long>(1) }
    val spots by viewModel.spots.collectAsState()
    val lots by viewModel.lots.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(selectedLot) {
        viewModel.loadSpotsByLot(selectedLot)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (error != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(onClick = { viewModel.retryLoading() }) {
                    Text("Retry")
                }
            }
        } else if (lots.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(modifier = Modifier.padding(innerPadding)) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    items(lots) { lot ->
                        Button(
                            onClick = { selectedLot = lot.id },
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .width(120.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedLot == lot.id)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text(text = lot.name)
                        }
                    }
                }

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(spots.size) { index ->
                        val spot = spots[index]
                        val backgroundColor = when (spot.status) {
                            SpotStatus.AVAILABLE -> Color.Green
                            SpotStatus.RESERVED -> Color(0xFFFFD700)  // Golden Yellow
                            SpotStatus.OCCUPIED -> Color.Red
                        }
                        val textColor = when (spot.status) {
                            SpotStatus.AVAILABLE, SpotStatus.RESERVED -> Color.Black
                            SpotStatus.OCCUPIED -> Color.White
                        }

                        Button(
                            onClick = {
                                if (spot.status == SpotStatus.AVAILABLE) {
                                    viewModel.reserveSpot(spot.id)
                                }
                            },
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                            modifier = Modifier
                                .width(150.dp)
                                .height(80.dp)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = buildString {
                                    append("Spot ${spot.spotNumber}\n")
                                    append(
                                        when (spot.status) {
                                            SpotStatus.AVAILABLE -> "Available"
                                            SpotStatus.RESERVED -> "Reserved"
                                            SpotStatus.OCCUPIED -> "Occupied"
                                        }
                                    )
                                    spot.vehicle?.let { append("\n$it") }
                                },
                                color = textColor,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(4.dp),
                                style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}