# Geo-Image Explorer

An android app that lets you view any location in the world, with its temperature and snapshots of nearby locations.

##### Table of Contents  



<a name="headers"/>


### Table of Contents
1. [Introduction](#Introduction) <a name="Introduction"/>
2. <ins>Features<ins>
3. <ins>Technology Stack<ins>
4. <ins>Getting Started<ins>
5. <ins>Usage<ins>
6. <ins>Feedback<ins>
7. <ins>Acknowledgements<ins>

### Introduction 

Developed as a part of our mobile development coursework, this Android application allows users to type in any location in the world, it then moves the camera to that location. It displays the current temperature there, and also snapshot images of nearby locations. It challenged us at every step, sharpening our problem solving and collaboration skills.

### Features

- A **google search bar** that allows you to search any location in the world. It also gives autocomplete suggestions of related locations.
- A **‘home’ button** that takes you back to your location, no matter where you are on the map.
- A **google maps view screen** that shows your location along with other points of information. The screen is also moveable manually by the user. 
- Shows the temperature of your desired location through a variety of different symbols i.e cloudy, sunny, etc.
- Shows image snapshots of nearby locations

### Technology Stack

  This android app was built in java. It uses the API:
  - OpenWeather (for the weather information) - https://openweathermap.org/
  - Windy (for the webcam) - https://api.windy.com/

### Getting Started

For installation, simply clone the repository, basically download it. You will need software that can run these android apps. Android Studio is recommended. You can also use alternative android simulators, like Bluestacks. 

To run the app using Android Studio, follow these steps:

   - Clone this repository to your local machine.
   - Open Android Studio and select "Open an existing Android Studio project."
   - Navigate to the project directory you just cloned and select the "build.gradle" file. It might highlight green with an android logo. 
   - Wait for Android Studio to sync the project, and then click the "Run" button to deploy the app on an emulator or physical device.

### Usage

Search for any location and get real time temperature data. <br><br>
<img height="500px" src="https://github.com/Zhredder/GeoImageExplorer/blob/main/Location%20Search%20Preview.PNG"> <br> <br>
Note: Currently the app only locates New Zealand locations. If you want to turn on global locations, you can do so by simply commenting out the code below which is in the "MapsActivity.java" file. It looks like this: 

 autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
    	autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
            	new LatLng(-48, 165),
            	new LatLng(-33, 180)));
    	autocompleteFragment.setCountries("NZ");

### Feedback

I am always looking to improve. If you have any constructive criticism or feedback for this project, then I would love to hear it!


### Acknowledgements

I would like to thank my project partner, Dylan Nunn, for putting in good effort towards this project. 

## Headers

