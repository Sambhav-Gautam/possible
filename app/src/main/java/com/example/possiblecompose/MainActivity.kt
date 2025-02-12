package com.example.possiblecompose

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.BufferedReader
import java.io.InputStreamReader

// Stop data model
data class Stop(val name: String, val visaRequired: Boolean, val distance: Double)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JourneyTrackerUI()
        }
    }
}

@Composable
fun JourneyTrackerUI() {
    val context = LocalContext.current
    var stops by remember { mutableStateOf(loadStopsFromFile(context)) }
    var selectedStart by remember { mutableStateOf(stops.firstOrNull()?.name ?: "") }
    var selectedEnd by remember { mutableStateOf(stops.lastOrNull()?.name ?: "") }
    var totalDistance by remember { mutableDoubleStateOf(0.0) }
    var distanceLeft by remember { mutableDoubleStateOf(0.0) }
    var progress by remember { mutableFloatStateOf(0f) }
    var currentStopIndex by remember { mutableIntStateOf(0) }
    var showStops by remember { mutableStateOf(false) }
    var isKm by remember { mutableStateOf(true) }


    // ✅ Remember LazyListState to handle auto-scrolling
    val listState = rememberLazyListState()
    var showDialog by remember { mutableStateOf(false) }

    // ✅ Auto-scroll to the current stop whenever it changes
    LaunchedEffect(currentStopIndex) {
        listState.animateScrollToItem(currentStopIndex)
    }

    fun resetJourney() {
        stops = loadStopsFromFile(context)
        selectedStart = stops.firstOrNull()?.name ?: ""
        selectedEnd = stops.lastOrNull()?.name ?: ""
        totalDistance = 0.0
        distanceLeft = 0.0
        progress = 0f
        currentStopIndex = 0
        showStops = false
        showDialog = false
        Toast.makeText(context, "Journey Reset", Toast.LENGTH_SHORT).show()
    }
    fun convertDistance(distance: Double, isKm: Boolean): String {
        return if (isKm) "%.2f".format(distance) else "%.2f".format(distance * 0.621371)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050404))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
            .padding(top = 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { resetJourney() }) {
                Text("Reset")
            }

            Button(onClick = { isKm = !isKm }) {
                Text(if (isKm) "KM" else "MI")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        DestinationDropdown("Start Destination", selectedStart, stops) { newStart ->
            selectedStart = newStart
            if (stops.indexOfFirst { it.name == selectedStart } > stops.indexOfFirst { it.name == selectedEnd }) {
                selectedEnd = newStart
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        DestinationDropdown("End Destination", selectedEnd, stops) { newEnd ->
            selectedEnd = newEnd
            if (stops.indexOfFirst { it.name == selectedEnd } < stops.indexOfFirst { it.name == selectedStart }) {
                selectedStart = newEnd
            }
        }

        Button(onClick = {
            totalDistance = calculateTotalDistance(stops, selectedStart, selectedEnd)
            distanceLeft = totalDistance
            showStops = true
        }, modifier = Modifier.padding(top = 12.dp)) {
            Text("Set Destinations")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text("Total Distance: ${convertDistance(totalDistance, isKm)} ${if (isKm) "km" else "miles"}", color = Color.White, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Text("Distance Left: ${convertDistance(distanceLeft, isKm)} ${if (isKm) "km" else "miles"}", color = Color.White, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(0.8f).height(10.dp),
            color = Color.Green,
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            val endIndex = stops.indexOfFirst { it.name == selectedEnd }
            if (showStops) {
                if (currentStopIndex < endIndex) {
                    currentStopIndex++
                    distanceLeft -= stops[currentStopIndex].distance
                    progress = (1.0 - (distanceLeft / totalDistance)).coerceIn(0.0, 1.0).toFloat()
                } else {
                    showDialog = true  // Show alert when final destination is reached
                }
            }
        }) {
            Text("Next Stop")
        }

        // Show an alert dialog when the journey ends
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Journey Completed") },
                text = { Text("You have reached your final destination!") },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }


        Spacer(modifier = Modifier.height(20.dp))
        if (showStops) {
        LazyColumn(modifier = Modifier.fillMaxHeight(),state = listState) {
            itemsIndexed(stops) { index, stop ->
                if (index in stops.indexOfFirst { it.name == selectedStart }..stops.indexOfFirst { it.name == selectedEnd }) {
                    StopItem(stop, isCurrent = index == currentStopIndex) {
                        if (index <= stops.indexOfFirst { it.name == selectedEnd }) {
                            currentStopIndex = index
                            distanceLeft = totalDistance - stops.subList(0, index + 1)
                                .sumOf { it.distance.toInt() }
                            progress = (1.0 - (distanceLeft / totalDistance)).toFloat()
                        }
                    }
                }
            }
        }
    }
    }
}



@Composable
fun DestinationDropdown(label: String, selected: String, stops: List<Stop>, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label, color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
        Box {
            Button(onClick = { expanded = true }) {
                Text(selected)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                stops.forEach { stop ->
                    DropdownMenuItem(
                        text = { Text(stop.name) },
                        onClick = {
                            onValueChange(stop.name)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

fun loadStopsFromFile(context: Context): List<Stop> {
    val stops = mutableListOf<Stop>()
    try {
        val inputStream = context.resources.openRawResource(R.raw.possible_stops)
        val reader = BufferedReader(InputStreamReader(inputStream))
        reader.useLines { lines ->
            lines.forEach { line ->
                val parts = line.split(",")
                if (parts.size == 3) {
                    stops.add(Stop(parts[0], parts[1].toBoolean(), parts[2].toDouble()))
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return stops.toList() // 🔥 Ensure returning a List, not mutableList
}

fun calculateTotalDistance(stops: List<Stop>, start: String, end: String): Double {
    val startIndex = stops.indexOfFirst { it.name == start }
    val endIndex = stops.indexOfFirst { it.name == end }
    return if (startIndex >= 0 && endIndex >= 0 && startIndex <= endIndex) {
        stops.subList(startIndex, endIndex + 1).sumOf { it.distance }
    } else 0.0
}


@Composable
fun StopItem(stop: Stop, isCurrent: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = if (isCurrent) Color(0xFFFFDDC1) else Color.DarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stop.name, textAlign = TextAlign.Center, color = Color.Black, fontSize = 18.sp)
            Text(text = "Visa Required: ${if (stop.visaRequired) "Yes" else "No"}", color = Color.Black, fontSize = 16.sp)
            Text(text = "Distance: ${stop.distance} KM", color = Color.Black, fontSize = 16.sp)
        }
    }
}
