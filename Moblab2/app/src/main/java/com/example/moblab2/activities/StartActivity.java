package com.example.moblab2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moblab2.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageButton buttonCar = findViewById(R.id.carButton);
        ImageButton buttonContacts = findViewById(R.id.contactButton);
        ImageButton buttonGeoService = findViewById(R.id.geoServiceButton);
        ImageButton buttonPersonalPage = findViewById(R.id.buttonPersonalPage);


        buttonCar.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, CarsActivity.class);
            startActivity(intent);
        });
        buttonContacts.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, ContactActivity.class);
            startActivity(intent);
        });

        buttonGeoService.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, GeoServiceActivity.class);
            startActivity(intent);
        });

        buttonPersonalPage.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, PersonalPageActivity.class);
            startActivity(intent);
        });
    }
}