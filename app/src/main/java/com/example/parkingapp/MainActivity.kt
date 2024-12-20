package com.example.parkingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ParkingAppTheme {
                ParkingAppScreen()
            }
        }
    }
}

@Composable
fun ParkingAppScreen() {
    val spots = remember { mutableStateListOf<ParkingSpot>() }

    LaunchedEffect(Unit) {
        if (spots.isEmpty()) {
            spots.addAll(listOf(
                ParkingSpot(1, "Spot 1", false, 1),
                ParkingSpot(2, "Spot 2", true, 1),
                ParkingSpot(3, "Spot 3", false, 1),
                ParkingSpot(4, "Spot 4", true, 1),
                ParkingSpot(5, "Spot 5", false, 1),
                ParkingSpot(6, "Spot 6", true, 1),
                ParkingSpot(7, "Spot 7", false, 1),
                ParkingSpot(8, "Spot 8", true, 1),
                ParkingSpot(9, "Spot 9", false, 1),
                ParkingSpot(10, "Spot 10", true, 1),
                ParkingSpot(11, "Spot 11", false, 1),
                ParkingSpot(12, "Spot 12", true, 1),
                ParkingSpot(13, "Spot 13", false, 1),
                ParkingSpot(14, "Spot 14", true, 1),
                ParkingSpot(15, "Spot 15", false, 1),
                ParkingSpot(16, "Spot 16", true, 1),
                ParkingSpot(17, "Spot 17", false, 1),
                ParkingSpot(18, "Spot 18", true, 1),
                ParkingSpot(19, "Spot 19", false, 1),
                ParkingSpot(20, "Spot 20", true, 1),
                ParkingSpot(21, "Spot 21", false, 1),
                ParkingSpot(22, "Spot 22", true, 1),
                ParkingSpot(23, "Spot 23", false, 1),
                ParkingSpot(24, "Spot 24", true, 1),
                ParkingSpot(25, "Spot 25", false, 2),
                ParkingSpot(26, "Spot 26", true, 2),
                ParkingSpot(27, "Spot 27", false, 2),
                ParkingSpot(28, "Spot 28", true, 2),
                ParkingSpot(29, "Spot 29", false, 2),
                ParkingSpot(30, "Spot 30", true, 2),
                ParkingSpot(31, "Spot 31", false, 2),
                ParkingSpot(32, "Spot 32", true, 2),
                ParkingSpot(33, "Spot 33", false, 2),
                ParkingSpot(34, "Spot 34", true, 2),
                ParkingSpot(35, "Spot 35", false, 2),
                ParkingSpot(36, "Spot 36", true, 2)
            ))
        }
    }

    var selectedFloor by remember { mutableStateOf(1) }
    val spotStatus = remember { mutableStateMapOf<Long, Boolean>() }

    LaunchedEffect(Unit) {
        spots.forEach { spot ->
            spotStatus[spot.id] = spot.isOccupied
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Row of buttons for selecting the floor
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { selectedFloor = 1 },
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(text = "Floor 1")
                }
                Button(
                    onClick = { selectedFloor = 2 },
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(text = "Floor 2")
                }
            }

            val filteredSpots = spots.filter { it.floor.toInt() == selectedFloor }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(filteredSpots.size) { index ->
                    val spot = filteredSpots[index]
                    val isOccupied = spotStatus[spot.id] ?: false
                    val backgroundColor = if (!isOccupied) Color.Green else Color.Red

                    Button(
                        onClick = {
                            spotStatus[spot.id] = !isOccupied
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                        modifier = Modifier
                            .width(150.dp)
                            .height(80.dp)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "${spot.id}\n${if (isOccupied) "Reserved" else "Available"}",
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

data class ParkingSpot(
    val id: Long,
    val name: String,
    var isOccupied: Boolean,
    var floor: Long
)
