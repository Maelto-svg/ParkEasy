package com.example.parkingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.parkingapp.ui.theme.ParkingAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set the content using Compose
        setContent {
            ParkingAppTheme {
                ParkingAppScreen() // Call the composable function here
            }
        }
    }
}

// Composable function that represents the screen
@Composable
fun ParkingAppScreen() {
    // Define mock parking spots using remember for state persistence
    val spots = remember { mutableStateListOf<ParkingSpot>() }

    // Add mock parking spots if empty
    LaunchedEffect(Unit) {
        if (spots.isEmpty()) {
            spots.addAll(listOf(
                ParkingSpot(1, "Spot 1", false),
                ParkingSpot(2, "Spot 2", true),
                ParkingSpot(3, "Spot 3", false),
                ParkingSpot(4, "Spot 4", true),
                ParkingSpot(5, "Spot 5", false),
                ParkingSpot(6, "Spot 6", true),
                ParkingSpot(7, "Spot 7", false),
                ParkingSpot(8, "Spot 8", true),
                ParkingSpot(9, "Spot 9", false),
                ParkingSpot(10, "Spot 10", true),
                ParkingSpot(11, "Spot 11", false),
                ParkingSpot(12, "Spot 12", true),
                ParkingSpot(13, "Spot 13", false),
                ParkingSpot(14, "Spot 14", true),
                ParkingSpot(15, "Spot 15", false),
                ParkingSpot(16, "Spot 16", true),
                ParkingSpot(17, "Spot 17", false),
                ParkingSpot(18, "Spot 18", true),
                ParkingSpot(19, "Spot 19", false),
                ParkingSpot(20, "Spot 20", true),
                ParkingSpot(21, "Spot 21", false),
                ParkingSpot(22, "Spot 22", true),
                ParkingSpot(23, "Spot 23", false),
                ParkingSpot(24, "Spot 24", true),
                ParkingSpot(25, "Spot 25", false),
                ParkingSpot(26, "Spot 26", true),
                ParkingSpot(27, "Spot 27", false),
                ParkingSpot(28, "Spot 28", true),
                ParkingSpot(29, "Spot 29", false),
                ParkingSpot(30, "Spot 30", true),
                ParkingSpot(31, "Spot 31", false),
                ParkingSpot(31, "Spot 32", true),
                ParkingSpot(23, "Spot 33", false),
                ParkingSpot(24, "Spot 34", true),
                ParkingSpot(25, "Spot 35", false),
                ParkingSpot(26, "Spot 36", true),
            ))
        }
    }

    // UI Content
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // LazyVerticalGrid to create 2 columns
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                // Create a button for each spot
                items(spots.size) { index ->
                    val spot = spots[index]
                    var currentStatus by remember { mutableStateOf(spot.isOccupied) }
                    var backgroundColor by remember {
                        mutableStateOf(if (!spot.isOccupied) Color.Green else Color.Red)
                    }

                    Button(
                        onClick = {
                            // Toggle the spot's status
                            val newStatus = !currentStatus
                            spot.isOccupied = newStatus
                            currentStatus = newStatus
                            backgroundColor = if (!newStatus) Color.Green else Color.Red
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                        modifier = Modifier
                            .width(150.dp)
                            .height(50.dp)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = if (currentStatus) "Reserved" else "Available",
                            color = if (backgroundColor == Color.Green) Color.Black else Color.White
                        )
                    }
                }
            }
        }
    }
}

// Data class to represent a parking spot
data class ParkingSpot(
    val id: Long,
    val name: String,
    var isOccupied: Boolean
)
