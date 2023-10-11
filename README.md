# Geo-Image Explorer

An android app that lets you view any location in the world, with its temperature and snapshots of nearby locations.


### Table of Contents
1. [Introduction](#Introduction) <a name="Introduction"/>
2. [Features](#Features) <a name="Features"/>
3. [Technology Stack](#TechnologyStack) <a name="TechnologyStack"/>
4. [Getting Started](#GettingStarted) <a name="GettingStarted"/>
5. [Usage](#Usage) <a name="Usage"/>
6. [Feedback](#Feedback) <a name="Feedback"/>
7. [Acknowledgements](#Acknowledgements) <a name="Acknowledgements"/>

### Introduction 

Developed as a part of our mobile development coursework, this Android application allows users to type in any location in the world, it then moves the camera to that location. It displays the current temperature there, and also snapshot images of nearby locations. It challenged us at every step, sharpening our problem solving and collaboration skills.

### Features

- A **google search bar** that allows you to search any location in the world. It also gives autocomplete suggestions of related locations.
- A **‘home’ button** that takes you back to your location, no matter where you are on the map.
- A **google maps view screen** that shows your location along with other points of information. The screen is also moveable manually by the user. 
- Shows the temperature of your desired location through a variety of different symbols i.e cloudy, sunny, etc.
- Shows image snapshots of nearby locations

### Technology Stack

  Built on the Android platform using Java. We integrated the following APIs:
  - OpenWeather (for the weather information) - https://openweathermap.org/
  - Windy (for the webcam) - https://api.windy.com/

### Getting Started

For installation, simply clone or download the repository. You will need software that can run these android apps. Android Studio is recommended. You can also use alternative android simulators, like Bluestacks. 

To run the app using Android Studio, follow these steps:

   - Clone this repository to your local machine.
   - Open Android Studio and select "Open an existing Android Studio project."
   - Navigate to the project directory you just cloned and select the "build.gradle" file. It might highlight green with an android logo. 
   - Wait for Android Studio to sync the project, and then click the "Run" button to deploy the app on an emulator or physical device.

### Usage

Search for any location and get real time temperature data. <br><br>
<img height="500px" src="https://github.com/Zhredder/GeoImageExplorer/blob/main/Location%20Search%20Preview.PNG"> <br> <br>
Important: The app is currently set to only search for locations within New Zealand. To enable global search, modify the following code in 'MapsActivity.java':"

 autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
    	autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
            	new LatLng(-48, 165),
            	new LatLng(-33, 180)));
    	autocompleteFragment.setCountries("NZ");

### Feedback

I am always looking to improve. If you have any constructive criticism or feedback for this project, then I would love to hear it! Feel free to open an issue or contact me directly through my website or through Github.


### Acknowledgements

I would like to thank my project partner, Dylan Nunn, for putting in good effort towards this project. 


