package com.visitegypt.presentation.gamification;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.Explore;
import com.visitegypt.domain.model.Hint;
import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Review;
import com.visitegypt.presentation.home.HomeRecyclerViewAdapter;
import com.visitegypt.presentation.home.child.discover.DiscoverPlaceAdapter;
import com.visitegypt.presentation.review.ReviewViewModel;
import com.visitegypt.utils.Constants;
import com.visitegypt.utils.GamificationRules;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import uk.co.senab.photoview.PhotoViewAttacher;

@AndroidEntryPoint
public class GamificationActivityy extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private static final String TAG = "Gamification Activity";
    @Inject
    public SharedPreferences sharedPreferences;
    String placeId;
    private ArrayList<PlaceActivity> placeActivities;
    private ArrayList<Badge> badges;
    private Dialog addReviewDialog;
    private ReviewViewModel reviewViewModel;

    private BadgesSliderViewAdapter badgesSliderViewAdapter;
    private RecyclerView recyclerView;
    private MaterialButton claimButton;
    private ArtifactsRecyclerViewAdapter artifactsRecyclerViewAdapter;
    private RecyclerView artifactsRecyclerView;
    private MapView mapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private double latitude, longitude;
    private boolean insideLocation = false;
    private GamificationViewModel gamificationViewModel;
    private Place place;
    private Explore dummyExplore;

    private Boolean mapLoaded = false, userLocationLoaded = false, placeLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamification);
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//
//        } else {
//            placeId = (String) savedInstanceState.getSerializable(DiscoverPlaceAdapter.CHOSEN_PLACE_ID);
//        }
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                placeId = null;
            } else {
                placeId = extras.getString(HomeRecyclerViewAdapter.CHOSEN_PLACE_ID);
            }
        } else {
            placeId = (String) savedInstanceState.getSerializable(HomeRecyclerViewAdapter.CHOSEN_PLACE_ID);
        }

        initViewModels(placeId);
        initPermissions();
        initViews();
        initDummyData();
    }

    private void initViewModels(String placeId) {
        gamificationViewModel = new ViewModelProvider(this).get(GamificationViewModel.class);
        gamificationViewModel.setPlaceId(placeId); // TODO
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        gamificationViewModel.setPlaceId(placeId);
        gamificationViewModel.getItemsByPlaceId(placeId);

        gamificationViewModel.itemMutableLiveData.observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                Log.d(TAG, "setting items to recycler view...");
                artifactsRecyclerViewAdapter.setItemsArrayList(items);
            }
        });
        try {
            gamificationViewModel.getPlaceDetail();
            gamificationViewModel.placesMutableLiveData.observe(this, new Observer<Place>() {
                @Override
                public void onChanged(Place place) {
                    GamificationActivityy.this.place = place;
                    placeLoaded = true;
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load, try again later", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void initViews() {
        placeActivities = new ArrayList<>();

        artifactsRecyclerView = findViewById(R.id.itemsGamificationActivityRecyclerView);
        artifactsRecyclerViewAdapter = new ArtifactsRecyclerViewAdapter(this);
        artifactsRecyclerView.setLayoutManager(new LinearLayoutManager(GamificationActivityy.this, LinearLayoutManager.HORIZONTAL, true));
        artifactsRecyclerView.setAdapter(artifactsRecyclerViewAdapter);

        recyclerView = findViewById(R.id.achievementsGamificationActivityRecyclerView);
        badges = new ArrayList<>();
        badgesSliderViewAdapter = new BadgesSliderViewAdapter(badges);
        recyclerView.setAdapter(badgesSliderViewAdapter);

        claimButton = findViewById(R.id.startExploringGamificationActivityButton);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void initPermissions() {
        if (ActivityCompat.checkSelfPermission(GamificationActivityy.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GamificationActivityy.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GamificationActivityy.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Log.d(TAG, "initPermissions: already granted");
        }
    }

    private void showExploreDialog(Explore explore) {
        Dialog dialog = new Dialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_ar_explore, null, false);

        ImageView zoomableImageView = v.findViewById(R.id.gamificationHintDialogZoomableImageView);

        MaterialTextView materialTextView = v.findViewById(R.id.gamificationHintDialogTitle);
        materialTextView.setText(explore.getTitle());

        Picasso.get().load(explore.getImageUrl()).into(zoomableImageView, new Callback() {
            @Override
            public void onSuccess() {
                // TODO ADD PROGRESSBAR
                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(zoomableImageView);
                photoViewAttacher.update();
            }

            @Override
            public void onError(Exception e) {

            }
        });

        GamificationHintRecyclerViewAdapter adapter = new GamificationHintRecyclerViewAdapter(explore.getHints());
        RecyclerView recyclerView = v.findViewById(R.id.gamificationHintDialogRecyclerView);
        recyclerView.setAdapter(adapter);

        dialog.setContentView(v);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
    private void showDialog() {
        View dialogLayout = LayoutInflater.from(GamificationActivityy.this).inflate(R.layout.dialog_add_review, null);
        addReviewDialog = new Dialog(this);
        addReviewDialog.setContentView(dialogLayout);
        addReviewDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addReviewDialog.show();
        addReviewDialog.findViewById(R.id.submitReviewButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText textInputEditText = (TextInputEditText) addReviewDialog.findViewById(R.id.reviewEditText);
                String reviewText = textInputEditText.getText().toString().trim();
                float numStars = ((RatingBar) addReviewDialog.findViewById(R.id.ratingBar)).getRating();
                Log.d(TAG, "onClick: " + reviewText);
                if (reviewText.isEmpty()) {
                    textInputEditText.setError("Review can't be empty");
                } else {

                    String firstName = sharedPreferences.getString(Constants.SHARED_PREF_FIRST_NAME, "");
                    String lastName = sharedPreferences.getString(Constants.SHARED_PREF_LAST_NAME, "");
                    String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
                    Review review = new Review(numStars, reviewText, firstName + " " + lastName, userId);
                    reviewViewModel.submitReview(placeId, review);
                    addReviewDialog.dismiss();
                }
            }

        });
    }

    public void addReview(View view) {
        showDialog();
    }
    private void showBadgeDialog(Badge badge) {
        Dialog dialog = new Dialog(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_badge_gamification, null, false);

        MaterialTextView titleTextView = v.findViewById(R.id.badgeDialogTitleTextView);
        titleTextView.setText(badge.getTitle());

        MaterialTextView descriptionTextView = v.findViewById(R.id.badgeDialogDescriptionTextView);
        descriptionTextView.setText(badge.getDescription());


        CircleProgressbar circleProgressbar = v.findViewById(R.id.badgeDialogCircleProgressBar);
        Log.d(TAG, "showLocationDialog: " + badge.getMaxProgress() + " " + badge.getProgress());
        circleProgressbar.setMaxProgress(badge.getMaxProgress());
        circleProgressbar.setProgress(badge.getProgress());
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                circleProgressbar.setBackground(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        Picasso.get().load(badge.getImageUrl()).into(target);

        RecyclerView recyclerView = v.findViewById(R.id.dialogBadgeRecyclerView);
        GamificationBadgesDialogRecyclerViewAdapter gamificationBadgesDialogRecyclerViewAdapter = new GamificationBadgesDialogRecyclerViewAdapter(badge.getBadgeTasks());
        recyclerView.setAdapter(gamificationBadgesDialogRecyclerViewAdapter);
        dialog.setContentView(v);

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void initDummyData() {
        placeActivities.add(new PlaceActivity(100, PlaceActivity.VISIT_LOCATION, "Visit Place", "Head there and open your location confirmation"));
        placeActivities.add(new PlaceActivity(150, PlaceActivity.POST_STORY, "Post a story", "Head there and post a story"));
        placeActivities.add(new PlaceActivity(100, PlaceActivity.POST_POST, "Post a post", "Head there and open post a post"));
        placeActivities.add(new PlaceActivity(100, PlaceActivity.ASK_CHAT_BOT, "Ask the chat bot", "Tell the bot \"What do you know about Luxor\""));
        placeActivities.add(new PlaceActivity(100, PlaceActivity.GENERAL, "General", "Dance or something"));

        badges.add(new Badge(0, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", false, Badge.Type.PLACE, 120));
        badges.add(new Badge(0, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", true, Badge.Type.PLACE, 120));
        badges.add(new Badge(0, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", false, Badge.Type.PLACE, 120));
        badges.add(new Badge(0, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", true, Badge.Type.PLACE, 120));
        badges.add(new Badge(0, "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", false, Badge.Type.PLACE, 120));

        BadgeTask badgeTask = new BadgeTask("https://www.citypng.com/public/uploads/preview/-1216105642094jeazr60ms.png", "Review the place", 3, 5);
        ArrayList<BadgeTask> badgeTasks = new ArrayList<>();
        badgeTasks.add(badgeTask);

        badges.get(0).setTitle("Amazing badge");
        badges.get(0).setDescription("an amazing badge for an amazing person");
        badges.get(0).setBadgeTasks(badgeTasks);


        ArrayList<Hint> hints = new ArrayList<>();
        hints.add(new Hint("He is super handsome"));
        hints.add(new Hint("Okay he's ugly, we lied", "https://file1.science-et-vie.com/var/scienceetvie/storage/images/1/0/4/104445/et-momie-revela-ses-secrets.jpg"));
        dummyExplore = new Explore("Tout Ankha Amon", "https://images.lpcdn.ca/924x615/201002/13/147005-momie-toutankhamon.jpg", hints);

        claimButton.setOnClickListener(view -> {
            //showBadgeDialog(badges.get(0));
            showExploreDialog(dummyExplore);
        });

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