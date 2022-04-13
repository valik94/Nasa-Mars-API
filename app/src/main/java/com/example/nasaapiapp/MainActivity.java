package com.example.nasaapiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NetworkingService.NetworkingListener {
NetworkingService networkingService;
JsonService jsonService;
Spinner ropinner;
NumberPicker itemPicker;
DatePicker datePicker;
ImageView img;
    String date = "";
    String rover = "";
    ArrayList<NasaPhoto> allPhotos = new ArrayList<>(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkingService = new NetworkingService();
        networkingService.listener = this;
        jsonService = new JsonService();
        ropinner = findViewById(R.id.rovers);
        itemPicker = findViewById(R.id.items);
        datePicker = findViewById(R.id.earthdatePicker);
        img = findViewById(R.id.image);
        String[] rovers = {"Opportunity","Curiosity","Spirit"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.rover_item,R.id.roverName,rovers);
        ropinner.setAdapter(adapter);


        ropinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                rover = rovers[i];
                getData(rover, date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        itemPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                networkingService.getImageData(allPhotos.get(i1).img_url);
            }
        });



        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                int month =  datePicker.getMonth() + 1; // 0 and 11
                date = datePicker.getYear() +"-"+ month +"-" + datePicker.getDayOfMonth();
              getData(rover,date);
            }
        });

        getData("Opportunity","2015-3-10");
    }

    public void getData(String rover, String date){
       networkingService.getNasaImages(rover,date);

    }
    @Override
    public void dataListener(String josnString) {
        allPhotos = jsonService.getNasaData(josnString);
        if (allPhotos.size() > 0) {
            networkingService.getImageData(allPhotos.get(0).img_url);

            String[] displayedValues = new String[allPhotos.size()];
            for (int i = 0; i < allPhotos.size(); i++) {
                displayedValues[i] = allPhotos.get(i).id + "";
            }
            itemPicker.setMinValue(0);
            itemPicker.setMaxValue(displayedValues.length - 1);
            itemPicker.setDisplayedValues(displayedValues);
        }

    }

    @Override
    public void imageListener(Bitmap image) {

        img.setImageBitmap(image);
    }
}