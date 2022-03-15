package com.visitegypt.presentation.gamification;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.utils.GamificationRules;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GamificationActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private static final String TAG = "Gamification Activity";

    private ArrayList<PlaceActivity> placeActivities;
    private ArrayList<Badge> badges;
    private BadgesSliderViewAdapter badgesSliderViewAdapter;
    private RecyclerView recyclerView;
    private MaterialButton claimButton;

    private MapView mapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private double latitude, longitude;

    private boolean insideLocation = false;

    private GamificationViewModel gamificationViewModel;

    private Place place;

    private Boolean mapLoaded = false, userLocationLoaded = false, placeLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamification);


        initViewModels();
        initPermissions();
        initViews(savedInstanceState);
        initDummyData();
    }


    private void initViewModels() {
        gamificationViewModel = new ViewModelProvider(this).get(GamificationViewModel.class);
        gamificationViewModel.setPlaceId("616f2746b817807a7a6c7167"); // TODO
        try {
            gamificationViewModel.getPlaceDetail();
            gamificationViewModel.placeMutableLiveData.observe(this, new Observer<Place>() {
                @Override
                public void onChanged(Place place) {
                    GamificationActivity.this.place = place;
                    placeLoaded = true;
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load, try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void initViews(Bundle b) {
        placeActivities = new ArrayList<>();
        recyclerView = findViewById(R.id.gamificationActivitiesRecyclerView);
        badges = new ArrayList<>();
        badgesSliderViewAdapter = new BadgesSliderViewAdapter(badges);
        recyclerView.setAdapter(badgesSliderViewAdapter);

        claimButton = findViewById(R.id.gamificationActivityClaimButton);
        claimButton.setOnClickListener(view -> {
            showLocationDialog(b);
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void initPermissions() {
        if (ActivityCompat.checkSelfPermission(GamificationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GamificationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GamificationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Log.d(TAG, "initPermissions: already granted");
        }
    }

    private void showLocationDialog(Bundle b) {
        Dialog dialog = new Dialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_location_gamification, null, false);
        mapView = v.findViewById(R.id.mapView);
        mapView.getMapAsync(GamificationActivity.this);
        dialog.setContentView(v);
        mapView.onCreate(b);

        /* v.findViewById(R.id.confirmLocationButtonGamificationDialog).setOnClickListener(view -> {

        }); */

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setOnDismissListener(dialogInterface -> mapView.onPause());
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
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: updating your location...");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        userLocationLoaded = true;
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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (place == null) {
            Toast.makeText(this, "still loading...", Toast.LENGTH_SHORT).show();
        } else {
            googleMap.setMinZoomPreference(15);
            googleMap.setMaxZoomPreference(20);

            LatLng location = new LatLng(place.getLatitude(), place.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(location).title("LUXOR"));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

            // Instantiating CircleOptions to draw a circle around the marker
            CircleOptions locationCircle = new CircleOptions();
            LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
            locationCircle.center(latLng);
            locationCircle.radius(GamificationRules.CONFIRM_LOCATION_CIRCLE_RADIUS);
            locationCircle.strokeColor(Color.YELLOW);
            locationCircle.fillColor(0x30ff0000);
            locationCircle.strokeWidth(2);
            googleMap.addCircle(locationCircle);

            CircleOptions userCircle = new CircleOptions();
            LatLng userLatLng = new LatLng(latitude, longitude);
            userCircle.center(userLatLng);
            userCircle.radius(1);
            userCircle.strokeColor(Color.BLACK);
            userCircle.fillColor(0x30ff0000);
            userCircle.strokeWidth(2);
            googleMap.addCircle(userCircle);
            Log.d(TAG, "Map functions called");

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng));

            float[] distance = new float[2];
            Location.distanceBetween(latitude, longitude, place.getLatitude(), place.getLongitude(), distance);
            insideLocation = distance[0] <= locationCircle.getRadius();
            mapLoaded = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapView != null) {
            mapView.onStop();
        }
    }
}