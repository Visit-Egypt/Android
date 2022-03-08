package com.visitegypt.presentation.gamification;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.PlaceActivity;

import java.util.ArrayList;

public class GamificationActivity extends AppCompatActivity implements LocationListener {

    private ArrayList<PlaceActivity> placeActivities;
    private ArrayList<Badge> badges;
    //private GamificationCardRecyclerViewAdapter gamificationCardRecyclerViewAdapter;
    private BadgesSliderViewAdapter badgesSliderViewAdapter;
    //private SliderView sliderView;
    private RecyclerView recyclerView;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamification);

        initPermissions();
        initViews();
        initDummyData();
    }

    private void initViews() {
        placeActivities = new ArrayList<>();

        //gamificationCardRecyclerViewAdapter = new GamificationCardRecyclerViewAdapter(placeActivities);
        recyclerView = findViewById(R.id.gamificationActivitiesRecyclerView);
        badges = new ArrayList<>();
        badgesSliderViewAdapter = new BadgesSliderViewAdapter(badges);
        //sliderView = findViewById(R.id.gamificationActivityBadgesSliderAdapter);
        //sliderView.setSliderAdapter(badgesSliderViewAdapter);
        recyclerView.setAdapter(badgesSliderViewAdapter);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }

    private void initPermissions() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                            } else {
                                // No location access granted.
                                Toast.makeText(this, "location permission required", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void initDummyData() {
        placeActivities.add(new PlaceActivity(100, PlaceActivity.VISIT_LOCATION, "Visit Place", "Head there and open your location confirmation"));
        placeActivities.add(new PlaceActivity(150, PlaceActivity.POST_STORY, "Post a story", "Head there and post a story"));
        placeActivities.add(new PlaceActivity(100, PlaceActivity.POST_POST, "Post a post", "Head there and open post a post"));
        placeActivities.add(new PlaceActivity(100, PlaceActivity.ASK_CHAT_BOT, "Ask the chat bot", "Tell the bot \"What do you know about Luxor\""));
        placeActivities.add(new PlaceActivity(100, PlaceActivity.GENERAL, "General", "Dance or something"));

        badges.add(new Badge(0, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", false, Badge.PLACE, 120));
        badges.add(new Badge(0, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", true, Badge.PLACE, 120));
        badges.add(new Badge(0, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", false, Badge.PLACE, 120));
        badges.add(new Badge(0, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", true, Badge.PLACE, 120));
        badges.add(new Badge(0, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", false, Badge.PLACE, 120));

        badgesSliderViewAdapter.setBadges(badges);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}