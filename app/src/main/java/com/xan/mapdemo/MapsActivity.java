package com.xan.mapdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gmap;
    SupportMapFragment mapFrag;
    private ArrayList<LatLng> latLngs = new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationrequest;
    Location lastlocation;
    Marker currentlocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        latLngs.add(new LatLng(16.827376, 96.130420));
        latLngs.add(new LatLng(16.827694, 96.130409));
        latLngs.add(new LatLng(16.828146, 96.132716));


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.getUiSettings().setZoomControlsEnabled(true);
        gmap.getUiSettings().setZoomGesturesEnabled(true);
        gmap.getUiSettings().setCompassEnabled(true);

        locationrequest = new LocationRequest();
        locationrequest.setInterval(100000);
        locationrequest.setFastestInterval(100000);
        locationrequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
             /* ***FusedLocationProvider usage 1 => get current location using locationcallback and locationrequest ******/
            fusedLocationProviderClient.requestLocationUpdates(locationrequest, locationCallback, Looper.myLooper());

            /* ***FusedLocationProvider usage 2 => get current location by getting lastlocation ******/
            /*fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title("Current location");
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            currentlocationMarker = gmap.addMarker(markerOptions);
                            gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            if (location != null) {
                                // Logic to handle location object
                            }
                        }
                    });*/
            gmap.setMyLocationEnabled(true);
        }



        for(int i=0;i<latLngs.size();i++){

            gmap.addMarker(new MarkerOptions().position(latLngs.get(i)).title("Marker"));
            gmap.moveCamera(CameraUpdateFactory.newLatLng(latLngs.get(i)));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }
    }
    LocationCallback locationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {

            List<Location> locationList = locationResult.getLocations();
            if(locationList.size()>0){
                Location location =locationList.get(locationList.size()-1);
                lastlocation = location;
                if(currentlocationMarker != null){
                    currentlocationMarker.remove();
                }

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current location");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                currentlocationMarker = gmap.addMarker(markerOptions);
                gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            }
        }
    };
}
