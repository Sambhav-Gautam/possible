# Possible - Travel Stops Tracker

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

## Overview
Possible is an Android application designed to help users plan and track their travel journey across multiple stops. The app allows users to select start and end destinations, view stop details (name, visa requirements, and distance), and monitor their progress with a dynamic progress bar. Built using Kotlin and Android SDK, it features a RecyclerView for displaying stops and a user-friendly interface for managing travel routes. The app reads stop data from a raw resource file (`possible_stops.txt`) and supports unit conversion between kilometers and miles.

This project is hosted on GitHub: [Sambhav-Gautam/possible](https://github.com/Sambhav-Gautam/possible).

## Features
- **Destination Selection**: Choose start and end destinations from a dropdown list of stops.
- **Stop Details**: View stop names, visa requirements, and distances in a RecyclerView.
- **Progress Tracking**: Monitor journey progress with a progress bar and real-time distance calculations.
- **Unit Conversion**: Toggle between kilometers and miles for distance display.
- **Visa Alerts**: Receive alerts for stops requiring a visa.
- **Journey Management**: Navigate to the next stop or reset the journey.
- **Error Handling**: Gracefully handles file loading errors with default stops and user notifications.

## Demo
https://github.com/user-attachments/assets/532d3309-6e13-4fb6-8891-7a6654bdb3d7


## Project Structure
The project consists of two main Kotlin files and a resource file:

- **`StopsAdapter.kt`**:
  - Defines a `Stop` data class (`name`, `visaRequired`, `distance`).
  - Implements a `RecyclerView.Adapter` to display stops with highlighted current stop.
  - Updates stop visuals based on the current stop index.

- **`MainActivity.kt`**:
  - Manages the main UI, including spinners, buttons, and RecyclerView.
  - Loads stop data from `possible_stops.txt` or uses default stops on failure.
  - Handles user interactions like setting destinations, switching units, navigating stops, and resetting the journey.
  - Updates total distance, distance left, and progress bar dynamically.

- **`res/raw/possible_stops.txt`**:
  - A text file containing stop data in the format: `name,visaRequired,distance`.
  - Example: `New York,false,0.0`.

## Prerequisites
- **Android Studio**: Version 4.0 or higher.
- **Kotlin**: Version 1.5 or higher.
- **Android SDK**: API level 21 (Lollipop) or higher.
- **Device/Emulator**: Android 5.0+ for testing.

## Setup Instructions
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Sambhav-Gautam/possible.git
   ```
2. **Open in Android Studio**:
   - Launch Android Studio and select "Open an existing project."
   - Navigate to the cloned `possible` directory.
3. **Sync Project**:
   - Click "Sync Project with Gradle Files" to download dependencies.
4. **Add Stop Data**:
   - Ensure `res/raw/possible_stops.txt` exists with stop data in the format: `name,visaRequired,distance`.
   - Example content:
     ```
     New York,false,0.0
     Washington D.C.,false,5.0
     Toronto,true,10.0
     Chicago,false,15.0
     Los Angeles,false,30.0
     ```
5. **Run the App**:
   - Connect an Android device or start an emulator.
   - Click "Run" in Android Studio to build and deploy the app.

## Usage
1. **Select Destinations**:
   - Use the start and end destination spinners to choose your journey’s start and end stops.
   - Click "Set Destinations" to initialize the journey.
2. **View Stops**:
   - The RecyclerView displays the list of stops between the selected destinations.
   - The current stop is highlighted with a colored background.
3. **Navigate Stops**:
   - Click "Next Stop" to move to the next stop in the journey.
   - Receive a visa alert if the current stop requires a visa.
4. **Toggle Units**:
   - Click the "KM/Miles" button to switch distance units.
5. **Track Progress**:
   - Monitor total distance, distance left, and progress via the progress bar.
6. **Reset Journey**:
   - Click "Reset" to clear the current journey and start over.

## Error Handling
- If `possible_stops.txt` fails to load, the app falls back to a default list of stops and displays an error dialog.
- Invalid destination selections (e.g., end stop before start stop) trigger an alert prompting the user to correct their input.

## Future Improvements
- **Dynamic Stop Loading**: Support loading stops from a remote API instead of a static file.
- **Enhanced UI**: Add animations for smoother transitions and improve visual design.
- **Data Persistence**: Save user journey progress using SharedPreferences or a local database.
- **Custom Stop Input**: Allow users to manually add stops via a form.
- **Map Integration**: Display stops on a map with route visualization.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact
For questions or feedback, reach out via the [GitHub Issues](https://github.com/Sambhav-Gautam/possible/issues) page.
