package com.visitegypt.presentation.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.presentation.chatbot.ChatbotActivity;
import com.visitegypt.ui.account.AccountFragment;
import com.visitegypt.ui.setting.SettingFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {
    Fragment selectedFragment;
    private static final String TAG = "Home Activity";

    private RecyclerView homeRecyclerView;
    private HomeRecyclerViewAdapter homeRecyclerViewAdapter;

    private RecyclerView homeTopRecyclerView;
    private HomeRecyclerViewAdapter homeTopRecyclerViewAdapter;

    private HomeViewModel homeViewModel;
    private ArrayList<Place> placesArrayList;
    private ArrayList<Place> ourFavouritesArrayList;

    private ShimmerFrameLayout allPlacesShimmer;
    private ShimmerFrameLayout mustGoBeforeYouDieShimmer;
    public BottomNavigationView bottomNavigationView;

    private FloatingActionButton chatbotFloatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        createDummyPlaces();
        initViewModel();

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    private void initViews() {
        placesArrayList = new ArrayList<>();
        ourFavouritesArrayList = new ArrayList<>();

        homeRecyclerView = findViewById(R.id.homeRecyclerView);
        homeRecyclerViewAdapter = new HomeRecyclerViewAdapter(placesArrayList, this);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeRecyclerView.setAdapter(homeRecyclerViewAdapter);

        homeTopRecyclerViewAdapter = new HomeRecyclerViewAdapter(ourFavouritesArrayList, this);
        homeTopRecyclerView = findViewById(R.id.horizontalHomeRecyclerView);
        homeTopRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        homeTopRecyclerView.setAdapter(homeTopRecyclerViewAdapter);

        allPlacesShimmer = findViewById(R.id.allPlacesShimmer);
        mustGoBeforeYouDieShimmer = findViewById(R.id.recommendationsShimmer);
        chatbotFloatingActionButton=findViewById(R.id.chatbotFloatingActionButton);


        chatbotFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ChatbotActivity.class));
            }
        });
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAllPlaces();

        homeViewModel.placesMutableLiveData.observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> placesList) {
                stopShimmerAnimation();
                setRecyclerViewsVisible();
                setShimmersGone();
                homeRecyclerViewAdapter.updatePlacesList(placesList);
            }
        });
    }

    private void createDummyPlaces() {
        Place place = new Place();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://nileholiday.com/wp-content/uploads/2019/10/All-Temples-Of-Egypt1.jpg");
        place.setImageUrls(arrayList);
        place.setTitle("Karnak Temple");
        place.setLongDescription("The Karnak Temple Complex, commonly known as Karnak, comprises a vast mix of decayed temples, chapels, pylons, and other buildings near Luxor, Egypt.");

        Place place2 = new Place();
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add("https://www.egypttoday.com/siteimages/Larg/202106010323272327.jpg");
        place2.setImageUrls(arrayList2);
        place2.setTitle("Masjid Al Hakim");
        place2.setLongDescription("The Mosque of al-Hakim, nicknamed al-Anwar, is a major Islamic religious site in Cairo, Egypt.");

        Place place3 = new Place();
        ArrayList<String> arrayList3 = new ArrayList<>();
        arrayList3.add("https://cdn2.civitatis.com/egipto/asuan/excursion-abu-simbel-grid.jpg");
        place3.setImageUrls(arrayList3);
        place3.setTitle("Abu Simbel Temples");
        place3.setLongDescription("Abu Simbel is two massive rock-cut temples in the village of Abu Simbel, Aswan Governorate, Upper Egypt, near the border with Sudan");

        ourFavouritesArrayList.add(place);
        ourFavouritesArrayList.add(place2);
        placesArrayList.add(place3);
        placesArrayList.add(new Place("Mat7af Gamed", "a great place for family gathering, built in 19th century by a dead man"));
        homeRecyclerViewAdapter.updatePlacesList(placesArrayList);
        homeTopRecyclerViewAdapter.updatePlacesList(ourFavouritesArrayList);
    }

    private void startShimmerAnimation() {
        allPlacesShimmer.startShimmerAnimation();
        mustGoBeforeYouDieShimmer.startShimmerAnimation();
    }

    private void stopShimmerAnimation() {
        allPlacesShimmer.stopShimmerAnimation();
        mustGoBeforeYouDieShimmer.stopShimmerAnimation();
    }

    private void setRecyclerViewsVisible() {
        homeRecyclerView.setVisibility(View.VISIBLE);
        homeTopRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setShimmersGone() {
        allPlacesShimmer.setVisibility(View.GONE);
        mustGoBeforeYouDieShimmer.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startShimmerAnimation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopShimmerAnimation();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            LinearLayout homeLinearLayout = (LinearLayout) findViewById(R.id.homeLinearLayout);

            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().remove(selectedFragment).commit();
                    homeLinearLayout.setVisibility(View.VISIBLE);
                    break;

                case R.id.setting:
                    selectedFragment = new SettingFragment();
                    homeLinearLayout.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.navFragment, selectedFragment).commit();
                    break;

                case R.id.account:
                    selectedFragment = new AccountFragment();
                    homeLinearLayout.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.navFragment, selectedFragment).commit();
                    break;
                case R.id.ar:

                    break;

            }
            return true;
        }
    };

}