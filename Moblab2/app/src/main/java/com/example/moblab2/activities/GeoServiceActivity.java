package com.example.moblab2.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codebyashish.googledirectionapi.AbstractRouting;
import com.codebyashish.googledirectionapi.ErrorHandling;
import com.codebyashish.googledirectionapi.RouteDrawing;
import com.codebyashish.googledirectionapi.RouteInfoModel;
import com.codebyashish.googledirectionapi.RouteListener;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import com.example.moblab2.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GeoServiceActivity extends AppCompatActivity
        implements OnMapReadyCallback, RouteListener {

    private LatLng endPoint, startPoint;
    boolean isPermissionGranted;
    GoogleMap gMap;
    FusedLocationProviderClient fusedLocationClient;
    Button buildRouteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geoservice);
        buildRouteBtn=findViewById(R.id.buildRouteBtn);

        Places.initialize(getApplicationContext(), "AIzaSyA7hMIDvW7ltoYf5hzFEU32FdosElRZ4FI");
        PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

        autocompleteFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                endPoint = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                gMap.addMarker(new MarkerOptions().position(endPoint));
            }
            @Override
            public void onError(Status status) {}
        });
        checkMyPermission();
        if (isPermissionGranted) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
            supportMapFragment.getMapAsync(GeoServiceActivity.this);
        }
    }
    private void geoLocate() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            startPoint = new LatLng(location.getLatitude(), location.getLongitude());
                            Toast.makeText(this, "Ready to build route", Toast.LENGTH_SHORT).show();
                            gotoLocation(location.getLatitude(), location.getLongitude());
                            gMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
                        } else {
                            Toast.makeText(this, "location is null", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to get current location", Toast.LENGTH_SHORT).show();
                    });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void gotoLocation(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        gMap.moveCamera(cameraUpdate);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        geoLocate();
        buildRouteBtn.setOnClickListener(v -> {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(endPoint);
            googleMap.clear();
            googleMap.addMarker(markerOptions);
            getRoute(endPoint, startPoint);
        });
    }

    private void getRoute(LatLng start, LatLng end) {
        try {
            RouteDrawing routeDrawing = new RouteDrawing.Builder()
                    .context(GeoServiceActivity.this)
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this).alternativeRoutes(true)
                    .waypoints(start, end)
                    .build();
            routeDrawing.execute();
        } catch (Exception e) {
            if (e instanceof IOException) {
                Toast.makeText(this, "Failed to get route: Network error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Unexpected error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRouteSuccess(ArrayList<RouteInfoModel> list, int indexing) {
        Toast.makeText(this, "Route Success", Toast.LENGTH_SHORT).show();

        PolylineOptions polylineOptions = new PolylineOptions();
        ArrayList<Polyline> polylines = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == indexing) {
                Log.e("TAG", "onRoutingSuccess: routeIndexing" + indexing);
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(12);
                polylineOptions.addAll(list.get(indexing).getPoints());
                polylineOptions.startCap(new RoundCap());
                polylineOptions.endCap(new RoundCap());
                Polyline polyline = gMap.addPolyline(polylineOptions);
                polylines.add(polyline);
            }
        }
    }

    private void checkMyPermission() {
        Dexter.withContext(GeoServiceActivity.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(GeoServiceActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
                isPermissionGranted = true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void onRouteFailure(ErrorHandling e) {
        Toast.makeText(this, "Route Failure", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRouteStart() {
        Toast.makeText(this, "Route Start", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRouteCancelled() {
        Toast.makeText(this, "Route Canceled", Toast.LENGTH_SHORT).show();
    }
}
