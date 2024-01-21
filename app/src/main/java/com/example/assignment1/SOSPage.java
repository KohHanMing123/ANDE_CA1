package com.example.assignment1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SOSPage extends AppCompatActivity {
    private Button sosButton;
    private int clickCount = 0;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sospage);

        // Initialize LocationManager and LocationListener
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            //to get coords
//            @Override
//            public void onLocationChanged(Location location) {
//                Log.d("LocationManager", "onLocationChanged called");
//                showToast("Location changed: " + location.getLatitude() + ", " + location.getLongitude());
//                // Handle location changes, but for now, we won't do anything here
//            }

            //to translate coords to location
            @Override
            public void onLocationChanged(Location location) {
                Log.d("LocationManager", "onLocationChanged called");

                // Reverse geocoding to get the address
                Geocoder geocoder = new Geocoder(SOSPage.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (addresses != null && ((List<?>) addresses).size() > 0) {
                        Address address = addresses.get(0);
                        String locationName = address.getAddressLine(0); // Get the full address
                        showToast("Current Location: " + locationName);
                    } else {
                        showToast("Unable to retrieve location name.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        // SOS button click listener
        sosButton = findViewById(R.id.sosButton);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount++;

                if (clickCount == 3) {
                    clickCount = 0;

                    // Check and request location updates
                    if (checkLocationPermission()) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);

                    }
                }
            }
        });
        setupPulsatingAnimation();

        ImageView sosBackArrow = findViewById(R.id.sosBackArrow);

        sosBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SOSPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupPulsatingAnimation() {
        final Animation pulse = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        pulse.setDuration(1000);
        pulse.setRepeatCount(Animation.INFINITE);
        pulse.setRepeatMode(Animation.REVERSE);

        sosButton.startAnimation(pulse);
    }

    // Check if location permissions are granted, and request if not
    private boolean checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return false;
        } else {
            return true;
        }
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListener);
    }


}