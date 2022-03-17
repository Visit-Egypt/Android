package com.visitegypt.presentation.gamification;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.visitegypt.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class Gam extends AppCompatActivity {

    private static final String TAG = "Gamification Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamificationn);


    }


}