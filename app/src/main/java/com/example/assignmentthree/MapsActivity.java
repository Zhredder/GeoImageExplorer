package com.example.assignmentthree;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.assignmentthree.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //creation of Variables to used throughout
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = "MapsActivity";
    private Marker ClickMarker;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    //variables for the search function
    SearchView searchView;
    AutocompleteSupportFragment autocompleteSupportFragment;
    //variable for the searched address
    LatLng destinationAddress;
    //variables for recording the weather data
    String weatherTemp;
    String weatherFeels;
    String weatherPressure;
    String weatherHumidity;

    //Five sets of variables to hold information for each of the five Cameras
    String CamCity0;
    String CamTitle0;
    String CamThumbnail0;
    LatLng CamLatLng0;

    String CamCity1;
    String CamTitle1;
    String CamThumbnail1;
    LatLng CamLatLng1;

    String CamCity2;
    String CamTitle2;
    String CamThumbnail2;
    LatLng CamLatLng2;

    String CamCity3;
    String CamTitle3;
    String CamThumbnail3;
    LatLng CamLatLng3;

    String CamCity4;
    String CamTitle4;
    String CamThumbnail4;
    LatLng CamLatLng4;


// Api keys for the weather and webcams
    String apiWebCamKey = "xji8uqPV7uvzUA2yXiQTJYOw4kztLMkp";
    String apiWeatherKey = "a750291555c4a5562df7847d0580c874";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //intializing search functions with placesAPI
        Places.initialize(this, getString(R.string.GoogleApiKey), Locale.ENGLISH);
        PlacesClient placesClient = Places.createClient(this);
        ////creating autocomplete fragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        //creating the filter the search operates under
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-48, 165),
                new LatLng(-33, 180)));
        autocompleteFragment.setCountries("NZ");
        //declaring what variables of a place are recorded
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        //listener for search completion
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.i("Error", "Error Occured: " + status);
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                //logging the information of the searched place
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng() + place.getAddress());
                //getting the Latitude and longitude of the searched location
                destinationAddress = place.getLatLng();
                //moving the camera to the searched location
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(destinationAddress,  DEFAULT_ZOOM);
                mMap.animateCamera(cu);
                //double variables to hold searched location lat and lng for the API Usage
                double destinationLat = destinationAddress.latitude;
                double destinationLng = destinationAddress.longitude;
                //creation of the API urls to be used in the search
                String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + destinationLat + "&lon=" + destinationLng + "&appid=a750291555c4a5562df7847d0580c874";
                String CamUrl = "https://api.windy.com/api/webcams/v2/list/country=NZ/nearby="+ destinationLat +","+ destinationLng +",100/orderby=distance/limit=5?show=webcams:location,image&key=xji8uqPV7uvzUA2yXiQTJYOw4kztLMkp";
                //request queue creation
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                //Json request creation for the webcam API
                JsonObjectRequest WebCamrequest = new JsonObjectRequest(Request.Method.GET, CamUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject CamResponse) {
                        //try-catch to handle any errors in the json
                        try {
                            //response broken down to first layer
                            JSONObject Baseobject = CamResponse.getJSONObject("result");
                            //response broken down to the webcam layer and into an array
                            JSONArray object = Baseobject.getJSONArray("webcams");
                            //individual webcam information taken from the array and into seperate json objects
                            JSONObject obj0 = object.getJSONObject(0);
                            JSONObject obj1 = object.getJSONObject(1);
                            JSONObject obj2 = object.getJSONObject(2);
                            JSONObject obj3 = object.getJSONObject(3);
                            JSONObject obj4 = object.getJSONObject(4);
                            //calling method to process the details into each webcam's variables
                            getWebCam0Details(obj0);
                            getWebCam1Details(obj1);
                            getWebCam2Details(obj2);
                            getWebCam3Details(obj3);
                            getWebCam4Details(obj4);
                            //Creating the Webcamera Markers
                            CreateCams();
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                //json request for the Weather Api
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //try-catch to handle any json errors
                        try {
                            //method to get the details of the weather situation from the json response
                            getWeatherDetails(response);
                            //get the icon for the temperature
                            JSONArray object = response.getJSONArray("weather");
                            for(int i=0; i<object.length(); i++)
                            {
                                JSONObject obj = object.getJSONObject(i);
                                String weatherMain = obj.getString("main");
                                //creation of the weather marker
                                setWeather(weatherMain);
                                Log.d("Weather status: ", weatherMain);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                //adding both to the queue
                queue.add(WebCamrequest);
                queue.add(request);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //creation of the onclick events to display the information when the camera markers are clicked
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //String to hold the information of the clicked markers title
                String CameraNumber = marker.getTitle();
                //creation of the intent to move activities
                Intent i = new Intent(MapsActivity.this, WebCamDisplay.class);
                //series of if statements to check if the clicked marker is a camera marker
                if (CameraNumber.equals("CameraZero")) {
                    //adding relevant information to the intent to be passed to the activity
                    i.putExtra("CityRegion", CamCity0);
                    i.putExtra("Thumbnail", CamThumbnail0);
                    i.putExtra("Title", CamTitle0);
                    //starting the intent
                    startActivity(i);
                } else if (CameraNumber.equals("CameraOne")) {
                    i.putExtra("CityRegion", CamCity1);
                    i.putExtra("Thumbnail", CamThumbnail1);
                    i.putExtra("Title", CamTitle1);
                    startActivity(i);
                } else if (CameraNumber.equals("CameraTwo")) {
                    i.putExtra("CityRegion", CamCity2);
                    i.putExtra("Thumbnail", CamThumbnail2);
                    i.putExtra("Title", CamTitle2);
                    startActivity(i);
                } else if (CameraNumber.equals("CameraThree")) {
                    i.putExtra("CityRegion", CamCity3);
                    i.putExtra("Thumbnail", CamThumbnail3);
                    i.putExtra("Title", CamTitle3);
                    startActivity(i);
                } else if (CameraNumber.equals("CameraFour")) {
                    i.putExtra("CityRegion", CamCity4);
                    i.putExtra("Thumbnail", CamThumbnail4);
                    i.putExtra("Title", CamTitle4);
                    startActivity(i);
                }
                return false;
            }
        });
        enableMyLocation();
    }
    //set of 5 Methods to process the information of each webcam
    public void getWebCam0Details(JSONObject Obj){
        //try-catch to handle any json errors
        try {
            //getting title information
            CamTitle0 = Obj.getString("title");
            //processing json information down to the thumbnail level
            JSONObject Image = Obj.getJSONObject("image");
            JSONObject Current = Image.getJSONObject("current");
            //getting the thumbnail
            CamThumbnail0 = Current.getString("thumbnail");
            //processing json to the location level
            JSONObject Location = Obj.getJSONObject("location");
            //getting the City, Region and LatLng of the Camera
            CamCity0 = Location.getString("city") + ", " + Location.getString("region");
            CamLatLng0 = new LatLng(Location.getDouble("latitude"), Location.getDouble("longitude"));
            } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }
    public void getWebCam1Details(JSONObject Obj){

        try {
            CamTitle1 = Obj.getString("title");
            JSONObject Image = Obj.getJSONObject("image");
            JSONObject Current = Image.getJSONObject("current");
            CamThumbnail1 = Current.getString("thumbnail");
            JSONObject Location = Obj.getJSONObject("location");
            CamCity1 = Location.getString("city") + ", " + Location.getString("region");
            CamLatLng1 = new LatLng(Location.getDouble("latitude"), Location.getDouble("longitude"));

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }
    public void getWebCam2Details(JSONObject Obj){

        try {
            CamTitle2 = Obj.getString("title");
            JSONObject Image = Obj.getJSONObject("image");
            JSONObject Current = Image.getJSONObject("current");
            CamThumbnail2 = Current.getString("thumbnail");
            JSONObject Location = Obj.getJSONObject("location");
            CamCity2 = Location.getString("city") + ", " + Location.getString("region");
            CamLatLng2 = new LatLng(Location.getDouble("latitude"), Location.getDouble("longitude"));
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }
    public void getWebCam3Details(JSONObject Obj){

        try {
            CamTitle3 = Obj.getString("title");
            JSONObject Image = Obj.getJSONObject("image");
            JSONObject Current = Image.getJSONObject("current");
            CamThumbnail3 = Current.getString("thumbnail");
            JSONObject Location = Obj.getJSONObject("location");
            CamCity3 = Location.getString("city") + ", " + Location.getString("region");
            CamLatLng3 = new LatLng(Location.getDouble("latitude"), Location.getDouble("longitude"));
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }
    public void getWebCam4Details(JSONObject Obj){

        try {
            CamTitle4 = Obj.getString("title");
            JSONObject Image = Obj.getJSONObject("image");
            JSONObject Current = Image.getJSONObject("current");
            CamThumbnail4 = Current.getString("thumbnail");
            JSONObject Location = Obj.getJSONObject("location");
            CamCity4 = Location.getString("city") + ", " + Location.getString("region");
            CamLatLng4 = new LatLng(Location.getDouble("latitude"), Location.getDouble("longitude"));
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }

    //Method to create the Camera Markers from the JSON information
    public void CreateCams() {

        Marker CamZero = mMap.addMarker(new MarkerOptions().position(CamLatLng0)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.camera))
                .title("CameraZero"));
        Marker CamOne =mMap.addMarker(new MarkerOptions().position(CamLatLng1)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.camera))
                .title("CameraOne"));
        Marker CamTwo = mMap.addMarker(new MarkerOptions().position(CamLatLng2)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.camera))
                .title("CameraTwo"));
        Marker CamThree = mMap.addMarker(new MarkerOptions().position(CamLatLng3)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.camera))
                .title("CameraThree"));
        Marker CamFour = mMap.addMarker(new MarkerOptions().position(CamLatLng4)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.camera))
                .title("CameraFour"));
    }

    //Method to get the Details on the Locations Weather from the JSON Response
    public void getWeatherDetails(JSONObject response){

        //try-catch to handle JSON errors
        try {
            JSONObject weatherDetails = response.getJSONObject("main");
            //getting the relevant information
            weatherTemp = weatherDetails.getString("temp");
            weatherFeels = weatherDetails.getString("feels_like");
            weatherPressure =  weatherDetails.getString("pressure");
            weatherHumidity =  weatherDetails.getString("humidity");

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }


    }

    //Method to create the Weather Marker From the JSON information
    public void setWeather(String weather){

        //Series of If statements for Marker creation for each type of weather
        if(weather.equals("Clear")){
            mMap.addMarker(new MarkerOptions().position(destinationAddress)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.clear))
                    .snippet("Temperature: " + weatherTemp +  "\n" +" Feels like: " + weatherFeels +
                            "\n" + " Pressure: " +  weatherPressure + "\n" + " Humidity: " + weatherHumidity)
                    .title("Location"));

        }
        if(weather.equals("Clouds")) {
            mMap.addMarker(new MarkerOptions().position(destinationAddress)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.clouds))
                    .snippet("Temperature: " + weatherTemp +  "\n" +" Feels like: " + weatherFeels +
                            "\n" + " Pressure: " +  weatherPressure + "\n" + " Humidity: " + weatherHumidity)
                    .title("Location"));
        }
        if(weather.equals("Drizzle")){
            mMap.addMarker(new MarkerOptions().position(destinationAddress)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.drizzle))
                    .snippet("Temperature: " + weatherTemp +  "\n" +" Feels like: " + weatherFeels +
                            "\n" + " Pressure: " +  weatherPressure + "\n" + " Humidity: " + weatherHumidity)
                    .title("Location"));
        }
        if(weather.equals("Rain")) {
            mMap.addMarker(new MarkerOptions().position(destinationAddress)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.rain))
                    .snippet("Temperature: " + weatherTemp +  "\n" +" Feels like: " + weatherFeels +
                            "\n" + " Pressure: " +  weatherPressure + "\n" + " Humidity: " + weatherHumidity)
                    .title("Location"));
        }
        if(weather.equals("Snow")){
            mMap.addMarker(new MarkerOptions().position(destinationAddress)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.snow))
                    .snippet("Temperature: " + weatherTemp +  "\n" +" Feels like: " + weatherFeels +
                            "\n" + " Pressure: " +  weatherPressure + "\n" + " Humidity: " + weatherHumidity)
                    .title("Location"));
        }
        if(weather.equals("Thunderstorm")) {
            mMap.addMarker(new MarkerOptions().position(destinationAddress)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.thunderstorm))
                    .snippet("Temperature: " + weatherTemp +  "\n" +" Feels like: " + weatherFeels +
                            "\n" + " Pressure: " +  weatherPressure + "\n" + " Humidity: " + weatherHumidity)
                    .title("Location"));
        }
        //method to format text for when clicked on
        formatText();
        }

        /* sourced from: https://stackoverflow.com/questions/13904651/android-google-maps-v2-how-to-add-marker-with-multiline-snippet*/
        public void formatText(){
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    LinearLayout info = new LinearLayout(MapsActivity.this);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(MapsActivity.this);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(MapsActivity.this);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
        }

      //Method to Enable the users Location
    private void enableMyLocation() {
            //checking if Permission has been Granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    //method to handle the permission results
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }









}


