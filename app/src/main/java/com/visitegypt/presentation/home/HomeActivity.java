package com.visitegypt.presentation.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.visitegypt.R;
import com.visitegypt.domain.model.Place;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "Home Activity";

    private RecyclerView homeRecyclerView;
    private HomeRecyclerViewAdapter homeRecyclerViewAdapter;

    private RecyclerView homeTopRecyclerView;
    private HomeRecyclerViewAdapter homeTopRecyclerViewAdapter;

    private HomeViewModel homeViewModel;
    private ArrayList<Place> placesArrayList;
    private ArrayList<Place> ourFavouritesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hideStatusBar();
        setContentView(R.layout.activity_home);

        initViews();
        createDummyPlaces();
        initActionBar();
        initViewModel();
    }

    private void initViews() {
        placesArrayList = new ArrayList<>();
        ourFavouritesArrayList = new ArrayList<>();

        homeRecyclerView = findViewById(R.id.homeRecyclerView);
        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(placesArrayList);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeRecyclerView.setAdapter(homeRecyclerViewAdapter);

        homeTopRecyclerViewAdapter = new HomeRecyclerViewAdapter(ourFavouritesArrayList);
        homeTopRecyclerView = findViewById(R.id.horizontalHomeRecyclerView);
        homeTopRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        homeTopRecyclerView.setAdapter(homeTopRecyclerViewAdapter);
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAllPlaces();
        homeViewModel.placesMutableLiveData.observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                homeRecyclerViewAdapter.updatePlacesList((ArrayList<Place>) o);
            }
        });
    }

    private void initActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void createDummyPlaces() {
        Place place = new Place();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://nileholiday.com/wp-content/uploads/2019/10/All-Temples-Of-Egypt1.jpg");
        place.setImageUrls(arrayList);
        place.setTitle("Karnak Temple");
        place.setDescription("The Karnak Temple Complex, commonly known as Karnak, comprises a vast mix of decayed temples, chapels, pylons, and other buildings near Luxor, Egypt.");

        Place place2 = new Place();
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add("https://www.egypttoday.com/siteimages/Larg/202106010323272327.jpg");
        place2.setImageUrls(arrayList2);
        place2.setTitle("Masjid Al Hakim");
        place2.setDescription("The Mosque of al-Hakim, nicknamed al-Anwar, is a major Islamic religious site in Cairo, Egypt.");

        Place place3 = new Place();
        ArrayList<String> arrayList3 = new ArrayList<>();
        arrayList3.add("https://cdn2.civitatis.com/egipto/asuan/excursion-abu-simbel-grid.jpg");
        place3.setImageUrls(arrayList3);
        place3.setTitle("Abu Simbel Temples");
        place3.setDescription("Abu Simbel is two massive rock-cut temples in the village of Abu Simbel, Aswan Governorate, Upper Egypt, near the border with Sudan");

        ourFavouritesArrayList.add(place);
        ourFavouritesArrayList.add(place2);
        placesArrayList.add(place3);
        placesArrayList.add(new Place("Mat7af Gamed", "a great place for family gathering, built in 19th century by a dead man"));
        homeRecyclerViewAdapter.updatePlacesList(placesArrayList);
        homeTopRecyclerViewAdapter.updatePlacesList(ourFavouritesArrayList);
    }
}