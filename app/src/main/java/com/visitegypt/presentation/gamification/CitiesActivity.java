package com.visitegypt.presentation.gamification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.visitegypt.R;
import com.visitegypt.presentation.place.PlacesActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class CitiesActivity extends AppCompatActivity {
    private static final String TAG = "Cities Activity";
    @Inject
    public SharedPreferences sharedPreferences;
    private ConstraintLayout luxorCityLayout, alexCityLayout, alsharkiaCityLayout, cairoCityLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        initViews();
        init();

    }


    private void initViews() {
        luxorCityLayout = findViewById(R.id.luxorCityLayout);
        alexCityLayout = findViewById(R.id.alexCityLayout);
        alsharkiaCityLayout = findViewById(R.id.alsharkiaCityLayout);
        cairoCityLayout = findViewById(R.id.cairoCityLayout);
    }

    private void init() {
        luxorCityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitiesActivity.this, PlacesActivity.class);
                intent.putExtra("city_name", "Luxor");
                startActivity(intent);

            }
        });
        cairoCityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitiesActivity.this, PlacesActivity.class);
                intent.putExtra("city_name", "Cairo");
                startActivity(intent);

            }
        });
        alsharkiaCityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitiesActivity.this, PlacesActivity.class);
                intent.putExtra("city_name", "Al-Sharkia");
                startActivity(intent);

            }
        });
        alexCityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CitiesActivity.this, PlacesActivity.class);
                intent.putExtra("city_name", "Alexandria");
                startActivity(intent);

            }
        });

    }
}