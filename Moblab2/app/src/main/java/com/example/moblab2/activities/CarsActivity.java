package com.example.moblab2.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moblab2.CarModel;
import com.example.moblab2.DataBaseHelper;
import com.example.moblab2.R;

public class CarsActivity extends AppCompatActivity {
    Button allCarsBtn, selectedCarsBtn;
    ListView carsListView;
    ArrayAdapter carsArrayAdapter;
    TextView editAverageEngineCapacity;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);

        allCarsBtn = findViewById(R.id.allCarsBtn);
        selectedCarsBtn = findViewById(R.id.selectedCarsBtn);
        carsListView = findViewById(R.id.carsListView);
        editAverageEngineCapacity = findViewById(R.id.editAverageEngineCapacity);

        dataBaseHelper = new DataBaseHelper(CarsActivity.this);

        //dataBaseHelper.deleteCars("Mercedes");
        //dataBaseHelper.deleteCars("Volkswagen");
        //dataBaseHelper.deleteCars("Honda");
        //dataBaseHelper.deleteCars("Audi");
        //dataBaseHelper.deleteCars("Toyota");

        //dataBaseHelper.addCar(new CarModel("Mercedes", "Sports", "Silver", 4.8, 100000));
        //dataBaseHelper.addCar(new CarModel("Volkswagen", "Universal", "Red", 1.8, 31000));
        //dataBaseHelper.addCar(new CarModel("Volkswagen", "Hatch—back", "Green", 2.0, 40000));
        //dataBaseHelper.addCar(new CarModel("Honda", "Hatch—back", "Black", 1.6, 14000));
        //dataBaseHelper.addCar(new CarModel("Audi", "Universal", "Red", 2.0, 30000));
        //dataBaseHelper.addCar(new CarModel("Toyota", "SUV", "Black", 1.7, 15000));
        //dataBaseHelper.addCar(new CarModel("Ford", "Pick-up", "Silver", 3.0, 50000));
        //dataBaseHelper.addCar(new CarModel("BMW", "Coupe", "Silver", 2.2, 50000));
        //dataBaseHelper.addCar(new CarModel("Ford", "Hatch—back", "Yellow", 2.4, 50000));
        //dataBaseHelper.addCar(new CarModel("Mitsubishi", "SUV", "Red", 2.4, 14000));

        allCarsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carsArrayAdapter = new ArrayAdapter<>(CarsActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getCars());
                carsListView.setAdapter(carsArrayAdapter);
                editAverageEngineCapacity.setText( "Середній об'єм двигуна всіх авто = " + String.valueOf(dataBaseHelper.getAvgEngineCapacity()));
            }
        });

        selectedCarsBtn.setOnClickListener(view -> {
            carsArrayAdapter = new ArrayAdapter<CarModel>(CarsActivity.this, android.R.layout.simple_list_item_1 , dataBaseHelper.getSelectedCars());
            carsListView.setAdapter(carsArrayAdapter);
            editAverageEngineCapacity.setText( "Середній об'єм двигуна вибраних авто = " + String.valueOf(dataBaseHelper.getAvgEngineCapacity()));
        });


    }
}