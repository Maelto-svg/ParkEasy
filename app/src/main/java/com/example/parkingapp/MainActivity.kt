package com.example.parkingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.parkingapp.dao.SpotDao
import com.example.parkingapp.entity.Spot
import com.example.parkingapp.ui.theme.ParkingAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var spotDao: SpotDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize the database and DAO
        val databaseProvider = AppDatabaseProvider(applicationContext)
        spotDao = databaseProvider.spotDao

        setContent {
            ParkingAppTheme {
                val spots = remember { mutableStateListOf<Spot>() }

                // Load the buttons from the database
                LaunchedEffect(Unit) {
                    lifecycleScope.launch {
                        val fetched = spotDao.findAll()
                        if (fetched.isEmpty()) {
                            // Insert default buttons if database is empty
                            spotDao.insertSpot(Spot(id = 0, spotnumber = 0, isoccupied = true, lot_id = 0))
                            spots.addAll(spotDao.findAll())
                        } else {
                            spots.addAll(fetched)
                        }
                    }
                }

                // UI Content
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        spots.forEach { spot ->
                            var currentStatus by remember { mutableStateOf(spot.isoccupied) }
                            var backgroundColor by remember {
                                mutableStateOf(if (!spot.isoccupied) Color.Green else Color.Red)
                            }

                            Button(
                                onClick = {
                                    lifecycleScope.launch {
                                        // Toggle button status and update the database
                                        val newStatus = !currentStatus
                                        spotDao.updateSpotStatus(spot.id, newStatus)
                                        currentStatus = newStatus
                                        backgroundColor =
                                            if (!newStatus) Color.Green else Color.Red
                                    }
                                },
                                shape = RectangleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(50.dp)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = if(currentStatus) "reserved" else "available",
                                    color = if (backgroundColor == Color.Green) Color.Black else Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
