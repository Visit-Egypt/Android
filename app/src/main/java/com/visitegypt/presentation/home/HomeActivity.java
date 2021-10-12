package com.visitegypt.presentation.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.visitegypt.R;
import com.visitegypt.domain.model.Place;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "Home Activity";

    private RecyclerView homeRecyclerView;
    private HomeRecyclerViewAdapter homeRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
    }

    private void initViews(){
        ArrayList<Place> arrayList = new ArrayList<>();
        arrayList.add(new Place("Mat7af Gamed", "a great place for family gathering, built in 19th century by a dead man"));
        homeRecyclerView = findViewById(R.id.homeRecyclerView);
        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(arrayList);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeRecyclerView.setAdapter(homeRecyclerViewAdapter);
    }
}