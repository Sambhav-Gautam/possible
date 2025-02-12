package com.example.possible

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var totalDistanceText: TextView
    private lateinit var distanceLeftText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var switchUnitsButton: Button
    private lateinit var nextStopButton: Button
    private lateinit var resetButton: Button
    private lateinit var stopsRecyclerView: RecyclerView
    private lateinit var startDestinationSpinner: Spinner
    private lateinit var endDestinationSpinner: Spinner
    private lateinit var setDestinationsButton: Button

    private var allStops = mutableListOf<Stop>()
    private var stopsList = listOf<Stop>()
    private var isKm = true
    private var currentStopIndex = 0
    private var totalDistance = 0.0
    private var distanceLeft = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        loadStopsFromFile()
        setupSpinners()
        setupClickListeners()
    }

    private fun initializeViews() {
        totalDistanceText = findViewById(R.id.totalDistanceText)
        distanceLeftText = findViewById(R.id.distanceLeftText)
        progressBar = findViewById(R.id.progressBar)
        switchUnitsButton = findViewById(R.id.switchUnitsButton)
        nextStopButton = findViewById(R.id.nextStopButton)
        resetButton = findViewById(R.id.resetButton)
        stopsRecyclerView = findViewById(R.id.stopsRecyclerView)
        startDestinationSpinner = findViewById(R.id.startDestinationSpinner)
        endDestinationSpinner = findViewById(R.id.endDestinationSpinner)
        setDestinationsButton = findViewById(R.id.setDestinationsButton)
    }



    private fun loadStopsFromFile() {
        try {
            val inputStream = resources.openRawResource(R.raw.possible_stops)
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.useLines { lines ->
                lines.forEach { line ->
                    val parts = line.split(",")
                    if (parts.size == 3) {
                        val name = parts[0]
                        val visaRequired = parts[1].toBoolean()
                        val distance = parts[2].toDoubleOrNull() ?: 0.0
                        allStops.add(Stop(name, visaRequired, distance))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showErrorDialog("Failed to load stops. Using default stops.")
            allStops = mutableListOf(
                Stop("New York", false, 0.0),
                Stop("Washington D.C.", false, 5.0),
                Stop("Toronto", true, 10.0),
                Stop("Chicago", false, 15.0),
                Stop("Los Angeles", false, 30.0)
            )
        }
    }


    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun setupSpinners() {
        val stopNames = allStops.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stopNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        startDestinationSpinner.adapter = adapter
        endDestinationSpinner.adapter = adapter
    }

    private fun setupRecyclerView() {
        stopsRecyclerView.layoutManager = LinearLayoutManager(this)
        stopsRecyclerView.adapter = StopsAdapter(stopsList, currentStopIndex)
    }

    private fun setupClickListeners() {
        switchUnitsButton.setOnClickListener {
            toggleUnits()
        }
        nextStopButton.setOnClickListener {
            moveToNextStop()
        }
        resetButton.setOnClickListener {
            resetJourney()
        }
        setDestinationsButton.setOnClickListener {
            setJourneyStops()
        }
    }

    private fun setJourneyStops() {
        val start = startDestinationSpinner.selectedItem.toString()
        val end = endDestinationSpinner.selectedItem.toString()

        val startIndex = allStops.indexOfFirst { it.name == start }
        val endIndex = allStops.indexOfFirst { it.name == end }

        if (startIndex >= endIndex) {
            AlertDialog.Builder(this)
                .setTitle("Invalid Input")
                .setMessage("Please select valid starting and ending destinations in order.")
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
            return
        }

        stopsList = allStops.subList(startIndex, endIndex + 1)


        totalDistance = 0.0

        distanceLeft = stopsList.sumOf { it.distance }

        currentStopIndex = 0

        setupRecyclerView()
        updateDistanceTexts()
    }

    private fun toggleUnits() {
        isKm = !isKm
        switchUnitsButton.text = if (isKm) "KM" else "Miles"
        updateDistanceTexts()
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun updateDistanceTexts() {
        val conversionFactor = if (isKm) 1.0 else 0.621371
        val unit = if (isKm) "KM" else "Miles"

        if (stopsList.isEmpty()) {
            totalDistanceText.text = "Total Distance: 0.00 $unit"
            distanceLeftText.text = "Distance Left: 0.00 $unit"
            nextStopButton.text = "Next: -"
            progressBar.progress = 0
            return
        }

        totalDistanceText.text = "Total Distance: ${String.format("%.2f", totalDistance * conversionFactor)} $unit"
        distanceLeftText.text = "Distance Left: ${String.format("%.2f", distanceLeft * conversionFactor)} $unit"
        nextStopButton.text = "Next: ${if (currentStopIndex < stopsList.size - 1) stopsList[currentStopIndex + 1].name else "End"}"

        progressBar.progress = ((totalDistance - distanceLeft) / totalDistance * 100).toInt()
    }




    private fun moveToNextStop() {
        if (currentStopIndex < stopsList.size - 1) {
            currentStopIndex++
            val currentStop = stopsList[currentStopIndex]


            totalDistance += stopsList[currentStopIndex ].distance


            distanceLeft -= stopsList[currentStopIndex ].distance

            updateDistanceTexts()

            (stopsRecyclerView.adapter as? StopsAdapter)?.updateCurrentStopIndex(currentStopIndex)
            stopsRecyclerView.smoothScrollToPosition(currentStopIndex)
            if (currentStop.visaRequired) {
                showVisaAlert(currentStop.name)
            }

            if (currentStopIndex == stopsList.size) {
                showFinalStopAlert()
            }
        }
    }


    private fun showVisaAlert(stopName: String) {
        AlertDialog.Builder(this)
            .setTitle("Visa Required!")
            .setMessage("You need a visa to enter $stopName.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showFinalStopAlert() {
        AlertDialog.Builder(this)
            .setTitle("Journey Complete!")
            .setMessage("Congratulations! You've reached the final stop.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun resetJourney() {
        stopsList = emptyList()
        totalDistance = 0.0
        distanceLeft = 0.0
        currentStopIndex = 0
        setupRecyclerView()
        updateDistanceTexts()
    }
}