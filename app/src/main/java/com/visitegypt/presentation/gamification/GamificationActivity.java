package com.visitegypt.presentation.gamification;

import static com.visitegypt.utils.Constants.PLACE_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.Explore;
import com.visitegypt.domain.model.Hint;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Review;
import com.visitegypt.presentation.chatbot.ChatbotActivity;
import com.visitegypt.presentation.home.HomeRecyclerViewAdapter;
import com.visitegypt.presentation.post.PostActivity;
import com.visitegypt.presentation.review.ReviewViewModel;
import com.visitegypt.utils.Constants;
import com.visitegypt.utils.GamificationRules;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import uk.co.senab.photoview.PhotoViewAttacher;

@AndroidEntryPoint
public class GamificationActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    private static final String TAG = "Gamification Activity";
    public static final String MSG_TYPE = "type";
    public static final String ARTIFACTS = "artifacts";
    public static final String INSIGHTS = "insights";
    public static final String PLACE_TITLE = "placeTitle" ;
    @Inject
    public SharedPreferences sharedPreferences;
    String placeId;
    private ArrayList<PlaceActivity> placeActivities;
    private ArrayList<Badge> badges;
    private Dialog addReviewDialog;
    private ReviewViewModel reviewViewModel;

    private BadgesSliderViewAdapter badgesSliderViewAdapter;
    private RecyclerView achievementsRecyclerView;
    private Button claimButton, confirmLocationButton;
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
    private Boolean mapLoaded = false, placeLoaded = false;
    private MutableLiveData<Boolean> userLocationLoaded;

    private ImageView placeImageView;
    private TextView placeTitleTextView, placeRemainingActivitiesTextView, placeXpTextView;
    private ImageButton reviewImageButton, postPostImageButton, postStoryImageButton;
    private TextView visitPlaceXpTextView;
    private TextView storyXpTextView, postXpTextView, reviewXpTextView;
    private TextView insightsXpTextView, artifactsXpTextView;
    private TextView distanceAwayTextView;
    private ImageButton askAboutInsightsImageButton, askAboutArtifactsImageButton;
    private LinearProgressIndicator placeProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamification);
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
        placeId = "616f2746b817807a7a6c7167";

        initViewModels(placeId, savedInstanceState);
        initPermissions();
        initViews();
        initClickListeners();
        initDummyData();
    }

    private void initViewModels(String placeId, Bundle b) {
        gamificationViewModel = new ViewModelProvider(this).get(GamificationViewModel.class);
        gamificationViewModel.setPlaceId(placeId); // TODO
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        gamificationViewModel.setPlaceId(placeId);
        gamificationViewModel.getItemsByPlaceId(placeId);

        gamificationViewModel.itemMutableLiveData.observe(this, items -> {
            Log.d(TAG, "setting items to recycler view...");
            artifactsRecyclerViewAdapter.setItemsArrayList(items);
        });
        try {
            gamificationViewModel.getPlaceDetail();
            gamificationViewModel.placesMutableLiveData.observe(this, place -> {
                GamificationActivity.this.place = place;
                Log.d(TAG, "initViewModels: loaded place: " + place.getTitle());
                placeLoaded = true;
                mapView.getMapAsync(this);
                mapView.onCreate(b);
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
        artifactsRecyclerView.setAdapter(artifactsRecyclerViewAdapter);

        achievementsRecyclerView = findViewById(R.id.achievementsGamificationActivityRecyclerView);
        badges = new ArrayList<>();
        badgesSliderViewAdapter = new BadgesSliderViewAdapter(badges, this);
        achievementsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        achievementsRecyclerView.setAdapter(badgesSliderViewAdapter);

        claimButton = findViewById(R.id.startExploringGamificationActivityButton);
        confirmLocationButton = findViewById(R.id.confirmLocationGamificationActivityButton);
        confirmLocationButton.setOnClickListener(view -> {
            confirmLocation();
        });


        placeImageView = findViewById(R.id.placeImageGamificationActivityImageView);
        placeTitleTextView = findViewById(R.id.placeTitleGamificationActivityTextView);
        placeRemainingActivitiesTextView = findViewById(R.id.placeRemainingGamificationActivityTextView);
        placeProgressIndicator = findViewById(R.id.placeRemainingProgressGamificationActivityProgressIndicator);

        reviewImageButton = findViewById(R.id.reviewGamificationActivityImageButton);
        postPostImageButton = findViewById(R.id.postGamificationActivityImageButton);
        postStoryImageButton = findViewById(R.id.postStoryGamificationActivityImageButton);

        placeXpTextView = findViewById(R.id.placeXPGamificationActivityTextView);
        visitPlaceXpTextView = findViewById(R.id.visitPlaceXpNumberGamificationActivity);
        storyXpTextView = findViewById(R.id.storyXpNumberGamificationActivity);
        postXpTextView = findViewById(R.id.postXpNumberGamificationActivity);
        reviewXpTextView = findViewById(R.id.reviewXpNumberGamificationActivity);
        insightsXpTextView = findViewById(R.id.insightsXpNumberGamificationActivity);
        artifactsXpTextView = findViewById(R.id.artifactsXpNumberGamificationActivity);

        askAboutArtifactsImageButton = findViewById(R.id.askAboutArtifactGamificationActivityImageButton);
        askAboutInsightsImageButton = findViewById(R.id.askAnubisGamificationActivityImageButton);

        distanceAwayTextView = findViewById(R.id.awayGamificationActivityTextView);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapView = findViewById(R.id.mapViewGamificationActivity);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        userLocationLoaded = new MutableLiveData<>();
    }

    private void showReview() {
        // TODO
    }

    private void showPostPost() {
        // TODO
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra(PLACE_ID, placeId);
        startActivity(intent);
    }

    private void showPostStory() {
        // TODO
        Toast.makeText(this, "Stay tuned, coming soon...", Toast.LENGTH_SHORT).show();
    }

    private void initActivityLogic(Place place) {
        Picasso.get().load(place.getImageUrls().get(0)).into(placeImageView);
        placeTitleTextView.setText(place.getTitle());
        placeXpTextView.setText(place.getMaxProgress());

        try {
            ArrayList<PlaceActivity> placeActivities = place.getPlaceActivities();
            int totalXp = 0, userProgress = 0, maxProgress = 0;
            for (PlaceActivity placeActivity : placeActivities) {
                totalXp += placeActivity.getXp();
                userProgress += placeActivity.getProgress();
                maxProgress += placeActivity.getMaxProgress();
                switch (placeActivity.getType()) {
                    case PlaceActivity.VISIT_LOCATION:
                        visitPlaceXpTextView.setText(placeActivity.getMaxProgress());
                        break;
                    case PlaceActivity.POST_REVIEW:
                        reviewXpTextView.setText(placeActivity.getMaxProgress());
                        break;
                    case PlaceActivity.ASK_CHAT_BOT:
                        insightsXpTextView.setText(placeActivity.getMaxProgress());
                        artifactsXpTextView.setText(placeActivity.getMaxProgress());
                        break;
                    case PlaceActivity.POST_POST:
                        postXpTextView.setText(placeActivity.getMaxProgress());
                        break;
                    case PlaceActivity.POST_STORY:
                        storyXpTextView.setText(placeActivity.getMaxProgress());
                        break;
                    case PlaceActivity.GENERAL:
                        // pass TODO
                        break;
                    default:
                        break;
                }
            }
            placeProgressIndicator.setMax(maxProgress);
            placeProgressIndicator.setProgress(userProgress, true);
            placeRemainingActivitiesTextView.setText(totalXp + "xp remaining");

        } catch (Exception e) {
            Log.e(TAG, "fillActivity: failed to get place activities xp", e);
        }


        // TODO: remaining from user progress
        //placeRemainingActivitiesTextView.setText(place);
    }

    private void initClickListeners() {
        reviewImageButton.setOnClickListener(view -> {
            showReviewDialog();
        });
        postPostImageButton.setOnClickListener(view -> {
            // TODO
            showPostPost();

        });
        postStoryImageButton.setOnClickListener(view -> {
            // TODO
            showPostStory();
            Toast.makeText(this, "Stay tuned", Toast.LENGTH_LONG);
        });
        askAboutArtifactsImageButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatbotActivity.class);
            intent.putExtra(PLACE_TITLE , place.getTitle());
            intent.putExtra(MSG_TYPE, ARTIFACTS);
            startActivity(intent);
        });
        askAboutInsightsImageButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatbotActivity.class);
            intent.putExtra(PLACE_TITLE , place.getTitle());
            intent.putExtra(MSG_TYPE, INSIGHTS);
            startActivity(intent);
        });

    }

    private void confirmLocation() {
        //mapView.onResume();
    }

    private void initPermissions() {
        if (ActivityCompat.checkSelfPermission(GamificationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GamificationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GamificationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

    private void showReviewDialog() {
        View dialogLayout = LayoutInflater.from(GamificationActivity.this).inflate(R.layout.dialog_add_review, null);
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


    private void initDummyData() {
        placeActivities.add(new PlaceActivity(100, PlaceActivity.VISIT_LOCATION, "Visit Place", "Head there and open your location confirmation"));
        placeActivities.add(new PlaceActivity(150, PlaceActivity.POST_STORY, "Post a story", "Head there and post a story"));
        placeActivities.add(new PlaceActivity(100, PlaceActivity.POST_POST, "Post a post", "Head there and open post a post"));
        placeActivities.add(new PlaceActivity(100, PlaceActivity.ASK_CHAT_BOT, "Ask the chat bot", "Tell the bot \"What do you know about Luxor\""));
        placeActivities.add(new PlaceActivity(100, PlaceActivity.GENERAL, "General", "Dance or something"));

        badges.add(new Badge("0", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", false, Badge.Type.PLACE, 120));
        badges.add(new Badge("0", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", true, Badge.Type.PLACE, 120));
        badges.add(new Badge("0", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", false, Badge.Type.PLACE, 120));
        badges.add(new Badge("1", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", true, Badge.Type.PLACE, 120));
        badges.add(new Badge("2", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Circle-icons-art.svg/1200px-Circle-icons-art.svg.png", false, Badge.Type.PLACE, 120));

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
        if (mapView != null && place != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: updating your location...");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        userLocationLoaded.setValue(true);
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
        Log.d(TAG, "onMapReady called");
        this.googleMap = googleMap;

        int currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active on device
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_light_style));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active on device
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_night_style));
                break;
        }

        if (place == null) {
            Toast.makeText(this, "still loading...", Toast.LENGTH_SHORT).show();
        } else {
            googleMap.setMinZoomPreference(15);
            googleMap.setMaxZoomPreference(20);

            LatLng location = new LatLng(place.getLatitude(), place.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(location).title(place.getTitle()));
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
            // move camera to place location
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

            userLocationLoaded.observe(this, userLocationReady -> {
                if (userLocationReady) {
                    float[] distance = new float[1];
                    Location.distanceBetween(latitude, longitude, place.getLatitude(), place.getLongitude(), distance);
                    Log.d(TAG, "onMapReady: " + distance[0] + " distance between you and location");

                    if (distance[0] > GamificationRules.CONFIRM_LOCATION_CIRCLE_RADIUS * 10) {
                        distanceAwayTextView.setText(String.format(Locale.CANADA, "You are %,.2f metres away!", distance[0]));
                    } else if (distance[0] > GamificationRules.CONFIRM_LOCATION_CIRCLE_RADIUS) {
                        distanceAwayTextView.setText(String.format(Locale.CANADA, "You are only %,.0f metres away!", distance[0]));
                    } else {
                        distanceAwayTextView.setText(String.format(Locale.CANADA, "You are inside, you may confirm now!", distance[0]));
                        confirmLocation();
                    }
                }
            });


            mapLoaded = true;
            mapView.onResume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null && place != null) {
            Log.d(TAG, "onResume: map resumed");
            mapView.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapView != null && place != null) {
            mapView.onStop();
        }
    }
}
