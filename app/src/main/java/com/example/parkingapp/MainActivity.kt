package com.example.parkingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
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
            // Show error state
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
            // Show loading state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(modifier = Modifier.padding(innerPadding)) {
                // Rest of your existing UI code...
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    lots.forEach { lot ->
                        Button(
                            onClick = { selectedLot = lot.id },
                            modifier = Modifier.width(120.dp),
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
                        val backgroundColor = if (!spot.isOccupied) Color.Green else Color.Red

                        Button(
                            onClick = {
                                viewModel.updateSpotStatus(spot.id, !spot.isOccupied)
                            },
                            shape = RectangleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                            modifier = Modifier
                                .width(150.dp)
                                .height(80.dp)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Spot ${spot.spotNumber}\n${if (spot.isOccupied) "Reserved" else "Available"}",
                                color = if (backgroundColor == Color.Green) Color.Black else Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(4.dp),
                                style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}

data class ParkingLot(
    val id: Long,
    val name: String,
    val location: String,
    val capacity: Int,
    val spots: List<String>
)

data class ParkingSpot(
    val id: Long,
    val spotNumber: String,
    val isOccupied: Boolean,
    val vehicle: String?,
    val lot: String
)

data class Vehicle(
    val id: Long,
    val plate: String,
    val type: String,
    val spotId: Long
)