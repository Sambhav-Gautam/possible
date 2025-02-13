# Journey Tracking App

## Overview

This app allows users to track their journey by selecting start and end destinations. It supports unit conversions (KM/MI), progress tracking via a progress bar, and dynamic stop selection. The user interface is built using Jetpack Compose, offering a modern and smooth experience.

## Features

- **TextBox Implementation**: Traditional and lazy loading textboxes for user input.
- **Buttons and Unit Conversion**:
  - Reset Button
  - Distance Unit Toggle (KM/MI)
  - Destination Selection
  - Journey Progression Button
- **User Interface**:
  - Properly aligned UI components
  - Interactive ProgressBar for tracking journey progress
  - Visually appealing color scheme with dynamic updates
- **Jetpack Compose**:
  - UI is built using Jetpack Compose for efficiency and reusability
  - Smooth scrolling and state management
- **Journey Navigation**:
  - Users must select an initial and final destination.
  - Once destinations are set, the "Set Destinations" button initializes the journey.
  - Users can travel both forward and backward between stops.
  - Only three stops are displayed at a time using lazy loading for efficient memory usage.
  - Clicking "Next Stop" advances the journey step by step.
  - Progress is reflected in the progress bar.
- **Packaging & Deployment**:
  - Proper README documentation
  - APK packaging and release management
- **Functionality**:
  - The app runs smoothly without crashes
  - Journey progress is correctly updated based on user interactions

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Sambhav-Gautam/Journey-Tracking-App.git
   ```
2. Open the project in Android Studio.
3. Build and run the application on an emulator or physical device.

## Usage

1. Select the start and end destinations from the dropdown menus.
2. Click the "Set Destinations" button.
3. Track progress through the progress bar.
4. Click "Next Stop" to proceed with the journey.
5. Convert distances between KM and MI as needed.
6. Reset the journey anytime using the "Reset" button.
7. Only three stops are displayed at a time to optimize performance.
8. Users can travel both forward and backward between stops.

## APK Packaging

- The app is properly packaged into an APK for easy installation.
- Ensure dependencies and configurations are set correctly before building the APK.

