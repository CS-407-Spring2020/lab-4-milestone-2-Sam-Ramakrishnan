package com.example.lab4milestone2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);
                
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



        if(Build.VERSION.SDK_INT>=23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else{
            startListening();
        }

//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location!=null){
            updateLocationInfo(location);
        }
    }

    @SuppressLint("MissingPermission")
    private void startListening() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
    }

    public void updateLocationInfo(Location location){
        Log.i("locationInfo", location.toString());

        TextView tvLat = findViewById(R.id.tv_latitude);
        TextView tvLong = findViewById(R.id.tv_long);
        TextView tvAlt = findViewById(R.id.tv_alt);
        TextView tvAccu = findViewById(R.id.tv_accuracy);

        tvLat.setText("Latitude: " + location.getLatitude());
        tvLong.setText("Longitude: "+ location.getLongitude());
        tvAlt.setText("Altitude: "+ location.getAltitude());
        tvAccu.setText("Accuracy: "+ location.getAccuracy());



        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                if(address.getSubThoroughfare()!=null)
                result.append(address.getSubThoroughfare()).append("\n");;

                if(address.getThoroughfare()!=null)
                result.append(address.getThoroughfare()).append("\n");;


                if(address.getLocality()!=null)
                result.append(address.getLocality()).append("\n");

                if(address.getPostalCode()!=null)
                result.append(address.getPostalCode()).append("\n");;

                if(address.getCountryName()!=null)
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        TextView tvAdd = findViewById(R.id.tv_address);
        tvAdd.setText("Address: "+ result.toString());
    }
}
