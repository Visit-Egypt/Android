package com.visitegypt.presentation.gamification;

import static com.visitegypt.domain.model.PlaceActivity.POST_REVIEW;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Review;
import com.visitegypt.presentation.chatbot.ChatbotActivity;
import com.visitegypt.presentation.home.HomeRecyclerViewAdapter;
import com.visitegypt.presentation.post.PostActivity;
import com.visitegypt.presentation.review.ReviewViewModel;
import com.visitegypt.utils.Constants;
import com.visitegypt.utils.GamificationRules;
import com.visitegypt.utils.MergeObjects;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GamificationActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    public static final String MSG_TYPE = "type";
    public static final String ARTIFACTS = "artifacts";
    public static final String INSIGHTS = "insights";
    public static final String PLACE_TITLE = "placeTitle";
    private static final String TAG = "Gamification Activity";

    @Inject
    public SharedPreferences sharedPreferences;

    private String placeId;
    private ArrayList<PlaceActivity> placeActivities;
    private ArrayList<Badge> placeBadges;
    private ArrayList<Badge> userBadges;
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
    private final boolean insideLocation = false;
    private GamificationViewModel gamificationViewModel;
    private Place place;

    private ShimmerFrameLayout sliderShimmerFrameLayout, claimPlaceShimmer, badgesShimmer;
    private ShimmerFrameLayout socialActivitiesShimmer, mapShimmer, confirmLocation;
    private ShimmerFrameLayout adventureShimmer, barShimmer, startExploringShimmer, askAnubisShimmer;

    private ImageView placeImageView;
    private TextView placeTitleTextView, placeRemainingActivitiesTextView, placeXpTextView;
    private ImageButton reviewImageButton, postPostImageButton, postStoryImageButton;
    private TextView visitPlaceXpTextView;
    private TextView storyXpTextView, postXpTextView, reviewXpTextView;
    private TextView insightsXpTextView, artifactsXpTextView;
    private TextView distanceAwayTextView;
    private ImageButton askAboutInsightsImageButton, askAboutArtifactsImageButton;
    private LinearProgressIndicator placeProgressIndicator;
    private LinearLayout shimmerLayout, gamificationLayout;
    private ConstraintLayout postCardConstraintLayout;

    private final MutableLiveData<Boolean> userLocationLoaded = new MutableLiveData<>();
    private final MutableLiveData<Boolean> placeBadgesLoaded = new MutableLiveData<>();
    private final MutableLiveData<Boolean> userBadgesLoaded = new MutableLiveData<>();
    private final MutableLiveData<Boolean> placeActivitiesLoaded = new MutableLiveData<>();
    private final MutableLiveData<Boolean> userPlaceActivitiesLoaded = new MutableLiveData<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamification);


        initPlaceId(savedInstanceState);
        initPermissions();
        initViews();
        initViewModels(placeId, savedInstanceState);
        initClickListeners();
    }

    private void initPlaceId(Bundle savedInstanceState) {
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
    }

    private void initViewModels(String placeId, Bundle b) {
        gamificationViewModel = new ViewModelProvider(this).get(GamificationViewModel.class);
        gamificationViewModel.setPlaceId(placeId); // TODO
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        gamificationViewModel.setPlaceId(placeId);
        gamificationViewModel.getItemsByPlaceId(placeId);


//        gamificationViewModel.itemMutableLiveData.observe(this, items -> {
//            Log.d(TAG, "setting items to recycler view...");
//            artifactsRecyclerViewAdapter.setExploreArrayList(items);
//        });
        try {
            gamificationViewModel.getPlaceDetail();
            gamificationViewModel.placesMutableLiveData.observe(this, place -> {
                GamificationActivity.this.place = place;
                GamificationActivity.this.placeActivities = place.getPlaceActivities();
                Log.d(TAG, "initViewModels: loaded place: " + place.getTitle());
                stopShimmerAnimation();
                setLayoutVisible();
                mapView.getMapAsync(this);
                mapView.onCreate(b);
                //placeTitleTextView.setText(place.getTitle());
                artifactsRecyclerViewAdapter.setExploreArrayList(place.getExplores());
                initActivityLogic();
            });
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load, try again later", Toast.LENGTH_SHORT).show();
        }

        initBadgesViewModel();
    }

    private void initPlaceActivitiesViewModel() {
        Log.d(TAG, "initPlaceActivitiesViewModel: loading...");
        gamificationViewModel.getUserPlaceActivity();

        /*** TODO REMOVE THIS TRASH ***/
        int placeXp = 0, numActivities = 0;
        for (PlaceActivity placeActivity : place.getPlaceActivities()) {
            placeXp += placeActivity.getXp();
            numActivities += 1;
        }
        placeProgressIndicator.setProgress(0);
        placeXpTextView.setText(MessageFormat.format("0/{0} XP", placeXp));
        placeRemainingActivitiesTextView.setText(MessageFormat.format("{0} remaining activities", numActivities));
        /*** TODO END ***/

        gamificationViewModel.userPlaceActivitiesMutableLiveData.observe(this, placeActivities -> {
            int totalActivities = 0, doneActivities = 0, totalXp = 0, doneXp = 0;
            Log.d(TAG, "initPlaceActivitiesViewModel: userPlaceActivities: " + new Gson().toJson(placeActivities));
            if (place.getPlaceActivities() != null)
                Log.d(TAG, "initPlaceActivitiesViewModel: placeActivities: " + new Gson().toJson(place.getPlaceActivities()));
            else
                Log.d(TAG, "initPlaceActivitiesViewModel:  no place activities");
            for (PlaceActivity userPlaceActivity : placeActivities) {
                for (PlaceActivity placeActivity : place.getPlaceActivities()) {
                    totalActivities += placeActivity.getMaxProgress();
                    totalXp += placeActivity.getXp();
                    if (placeActivity.getId().equals(userPlaceActivity.getId())) {
                        MergeObjects.MergeTwoObjects.merge(placeActivity, userPlaceActivity);
                        Log.d(TAG, "initPlaceActivitiesViewModel: " + new Gson().toJson(placeActivity));
                        doneActivities += placeActivity.getProgress();
                        if (placeActivity.isFinished()) {
                            doneXp += placeActivity.getXp();
                        }
                        //placeActivity.setProgress(userPlaceActivity.getProgress());
                    }
                }
            }

            Log.d(TAG, "initPlaceActivitiesViewModel: total activities: " + totalActivities);
            Log.d(TAG, "initPlaceActivitiesViewModel: done activities: " + doneActivities);
            Log.d(TAG, "initPlaceActivitiesViewModel: total xp: " + totalXp);
            Log.d(TAG, "initPlaceActivitiesViewModel: done xp: " + doneXp);
            
            placeProgressIndicator.setMax(totalXp);
            placeProgressIndicator.setProgress(doneXp, true);

            if (totalActivities > doneActivities && (totalActivities - doneActivities) > 1)
                placeRemainingActivitiesTextView.setText(MessageFormat.format("{0} remaining activities", totalActivities - doneActivities));
            else if ((totalActivities - doneActivities) == 1) {
                placeRemainingActivitiesTextView.setText("1 remaining activity");
            } else {
                placeRemainingActivitiesTextView.setText("Completed");
            }
            placeXpTextView.setText(MessageFormat.format("{0}/{1} XP", doneXp, totalXp));
        });
    }

    private void initBadgesViewModel() {
        initBadges();
    }

    private void initBadges() {
        gamificationViewModel.setPlaceId(placeId);
        gamificationViewModel.getBadgesOfUser();
        gamificationViewModel.getPlaceBadges();
        gamificationViewModel.placeBadgesMutableLiveData.observe(this, placeBadges -> {
            this.placeBadges = (ArrayList<Badge>) placeBadges;
            Log.d(TAG, "initBadgesViewModel: place badges: " + new Gson().toJson(placeBadges));
            //Log.d(TAG, "initViewModel: BOOM" + new Gson().toJson(placeBadges.get(1).getBadgeTasks()));
            gamificationViewModel.userBadgesMutableLiveData.observe(this,
                    userBadges -> {
                        //Log.d(TAG, "initViewModel: BOOM" + new Gson().toJson(userBadges.get(1).getBadgeTasks()));
                        Log.d(TAG, "initViewModel: ");
                        for (Badge badge : userBadges) {
                            for (Badge placeBadge : placeBadges) {
                                if (badge.getId().equals(placeBadge.getId())) {
                                    // merge badge
                                    Log.d(TAG, "initBadgesViewModel: before original merge badge" + new Gson().toJson(placeBadge));
                                    Log.d(TAG, "initBadgesViewModel: before merge update badge" + new Gson().toJson(badge));

                                    MergeObjects.MergeTwoObjects.merge(placeBadge, badge);

                                    for (BadgeTask badgeTask : badge.getBadgeTasks()) {
                                        for (BadgeTask placeBadgeTask : placeBadge.getBadgeTasks()) {
                                            if (badgeTask.getTaskTitle().equals(placeBadgeTask.getTaskTitle())) {
                                                // same badgeTask -> merge them
                                                MergeObjects.MergeTwoObjects.merge(placeBadgeTask, badgeTask);
                                            }
                                        }
                                    }
                                    Log.d(TAG, "initBadgesViewModel: merged badge" + new Gson().toJson(placeBadge));

                                }
                            }
                        }
                        badgesSliderViewAdapter.setBadges((ArrayList<Badge>) placeBadges);
                    }
            );
        });
    }

    @SuppressLint("MissingPermission")
    private void initViews() {
        placeActivities = new ArrayList<>();

        artifactsRecyclerView = findViewById(R.id.itemsGamificationActivityRecyclerView);
        artifactsRecyclerViewAdapter = new ArtifactsRecyclerViewAdapter(this);
        artifactsRecyclerView.setAdapter(artifactsRecyclerViewAdapter);

        achievementsRecyclerView = findViewById(R.id.achievementsGamificationActivityRecyclerView);
        placeBadges = new ArrayList<>();
        userBadges = new ArrayList<>();
        badgesSliderViewAdapter = new BadgesSliderViewAdapter(placeBadges, this);
        achievementsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        achievementsRecyclerView.setAdapter(badgesSliderViewAdapter);
        /***********************************************************************/
        sliderShimmerFrameLayout = findViewById(R.id.sliderShimmerFrameLayout);
        claimPlaceShimmer = findViewById(R.id.claimPlaceShimmer);
        badgesShimmer = findViewById(R.id.badgesShimmer);
        socialActivitiesShimmer = findViewById(R.id.socialActivitiesShimmer);
        mapShimmer = findViewById(R.id.mapShimmer);
        confirmLocation = findViewById(R.id.confirmLocation);
        adventureShimmer = findViewById(R.id.adventautreShimmer);
        barShimmer = findViewById(R.id.barShimmer);
        startExploringShimmer = findViewById(R.id.startExploarShimmer);
        askAnubisShimmer = findViewById(R.id.askAnubisShimmer);
        shimmerLayout = findViewById(R.id.shimmerLayout);
        gamificationLayout = findViewById(R.id.gamificationLayout);
        startShimmerAnimation();
        /*******************************************************************/
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

        postCardConstraintLayout = findViewById(R.id.postCardConstraintLayout);
        postCardConstraintLayout.setBackgroundResource(R.drawable.card_review_edge);
    }


    private void showPostPost() {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra(PLACE_ID, placeId);
        startActivity(intent);
    }

    private void showPostStory() {
        Toast.makeText(this, "Stay tuned, coming soon...", Toast.LENGTH_SHORT).show();
    }

    private void initActivityLogic() {
        placeTitleTextView.setText(place.getTitle());
        if (place.getDefaultImage() == null) {
            Picasso.get().load(place.getImageUrls().get(0)).into(placeImageView);
        } else {
            Picasso.get().load(place.getDefaultImage()).into(placeImageView);
        }

        try {
            ArrayList<PlaceActivity> placeActivities = place.getPlaceActivities();
            Log.d(TAG, "initActivityLogic: " + new Gson().toJson(placeActivities));
            int totalXp = 0, maxProgress = 0;
            for (PlaceActivity placeActivity : placeActivities) {
                totalXp += placeActivity.getXp();
                maxProgress += placeActivity.getMaxProgress();
                switch (placeActivity.getType()) {
                    case PlaceActivity.VISIT_LOCATION:
                        visitPlaceXpTextView.setText(placeActivity.getMaxProgress());
                        break;
                    case POST_REVIEW:
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
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "fillActivity: failed to get place activities xp: " + e.getMessage());
        }

        try {
            if (place.getPlaceActivities() != null) {
                initPlaceActivitiesViewModel();
            } else {
                Log.d(TAG, "initActivityLogic: gamification not supported yet for this place");
                Toast.makeText(this, "gamification not supported for this place yet", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d(TAG, "initActivityLogic: couldn't init place activities: " + e.getMessage());
        }


        // set user progress TODO
//        gamificationViewModel.userPlaceActivitiesMutableLiveData.observe(this, placeActivities -> {
//            try {
//                int userProgress;
//                for (PlaceActivity placeActivity : placeActivities) {
//                    switch (placeActivity.getType()) {
//                        case PlaceActivity.VISIT_LOCATION:
//                            if (placeActivity.getProgress() == placeActivity.getMaxProgress())
//                                distanceAwayTextView.setText("Complete");
//                            break;
//                        case POST_REVIEW:
//                            if (placeActivity.getProgress() == placeActivity.getMaxProgress()) {
//                                // TODO user finished reviewing
//                            }
//                            break;
//                        case PlaceActivity.ASK_CHAT_BOT:
//                            if (placeActivity.getProgress() == placeActivity.getMaxProgress()) {
//                                // TODO user finsihed chatbot
//                            }
//                            break;
//                        case PlaceActivity.POST_POST:
//                            if (placeActivity.getProgress() == placeActivity.getMaxProgress()) {
//                                // TODO user finished posting
//                            }
//                            postXpTextView.setText(placeActivity.getMaxProgress());
//                            break;
//                        case PlaceActivity.POST_STORY:
//                            break;
//                        case PlaceActivity.GENERAL:
//                            // pass
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            } catch (Exception ignored) {
//            }
//        });

        // TODO: remaining from user progress
        //placeRemainingActivitiesTextView.setText(place);
    }

    private void initClickListeners() {
        reviewImageButton.setOnClickListener(view -> {
            showReviewDialog();
        });
        postPostImageButton.setOnClickListener(view -> {
            showPostPost();
        });
        postStoryImageButton.setOnClickListener(view -> {
            showPostStory();
        });
        askAboutArtifactsImageButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatbotActivity.class);
            intent.putExtra(PLACE_TITLE, place.getTitle());
            intent.putExtra(MSG_TYPE, ARTIFACTS);
            startActivity(intent);
        });
        askAboutInsightsImageButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatbotActivity.class);
            intent.putExtra(PLACE_TITLE, place.getTitle());
            intent.putExtra(MSG_TYPE, INSIGHTS);
            startActivity(intent);
        });
    }

    // call whenever you want to increase an activity's progress
    private void updatePlaceActivityProgress(PlaceActivity placeActivity) {
        gamificationViewModel.setPlaceActivity(placeActivity);
        try {
            gamificationViewModel.updatePlaceActivityForUser();
        } catch (Exception e) {
            // TODO store offline
            e.printStackTrace();
        }
    }

    private void confirmLocation() {
        //mapView.onResume();
        if (placeActivities != null) {
            for (PlaceActivity placeActivity : placeActivities) {
                if (placeActivity.getType().equals(PlaceActivity.VISIT_LOCATION)) {
                    updatePlaceActivityProgress(placeActivity);
                }
            }
        }
    }

    private void initPermissions() {
        if (ActivityCompat.checkSelfPermission(GamificationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GamificationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GamificationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Log.d(TAG, "initPermissions: already granted");
        }
    }

    private void showReviewDialog() {
        View dialogLayout = LayoutInflater.from(GamificationActivity.this).inflate(R.layout.dialog_add_review, null);
        addReviewDialog = new Dialog(this);
        addReviewDialog.setContentView(dialogLayout);
        addReviewDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addReviewDialog.show();
        addReviewDialog.findViewById(R.id.submitReviewButton).setOnClickListener(view -> {
            TextInputEditText textInputEditText = addReviewDialog.findViewById(R.id.reviewEditText);
            String reviewText = textInputEditText.getText().toString().trim();
            float numStars = ((RatingBar) addReviewDialog.findViewById(R.id.ratingBar)).getRating();
            Log.d(TAG, "submitting review: : " + reviewText);
            if (reviewText.isEmpty()) {
                textInputEditText.setError("Review can't be empty");
            } else {
                String firstName = sharedPreferences.getString(Constants.SHARED_PREF_FIRST_NAME, "");
                String lastName = sharedPreferences.getString(Constants.SHARED_PREF_LAST_NAME, "");
                String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
                Review review = new Review(numStars, reviewText, firstName + " " + lastName, userId);

                for (PlaceActivity placeActivity : placeActivities) {
                    if (placeActivity.getType().equals(POST_REVIEW)) {
                        Log.d(TAG, "showReviewDialog: found activity review: " + new Gson().toJson(placeActivity));
                        reviewViewModel.setReviewPlaceActivity(placeActivity);
                        //gamificationViewModel.setPlaceActivity(placeActivity);

                    }
                }

                Badge reviewBadge = null;
                for (Badge badge : placeBadges) {
                    if (badge.getType().equals("review")) {

                    }
                }

                //reviewViewModel.setBadgeTask(placeBadges.get());

                //reviewViewModel.setPlaceActivity(place);
                try {
                    //gamificationViewModel.updatePlaceActivityForUser();
                    reviewViewModel.submitReview(placeId, review);
                    addReviewDialog.dismiss();
                } catch (Exception e) {
                    Log.e(TAG, "showReviewDialog: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
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

                    if (distance[0] > GamificationRules.CONFIRM_LOCATION_CIRCLE_RADIUS) {
                        distanceAwayTextView.setText(String.format(Locale.CANADA, "You are %,.2f metres away!", distance[0]));
                    } else if (distance[0] > GamificationRules.CONFIRM_LOCATION_CIRCLE_RADIUS) {
                        distanceAwayTextView.setText(String.format(Locale.CANADA, "You are only %,.0f metres away!", distance[0]));
                    } else {
                        distanceAwayTextView.setText(String.format(Locale.CANADA, "You are inside, you may confirm now!", distance[0]));
                        confirmLocation();
                    }
                }
            });

            mapView.onResume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startShimmerAnimation();
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

    @Override
    public void onPause() {
        super.onPause();
        stopShimmerAnimation();
    }


    private void startShimmerAnimation() {
        claimPlaceShimmer.startShimmerAnimation();
        sliderShimmerFrameLayout.startShimmerAnimation();
        badgesShimmer.startShimmerAnimation();
        socialActivitiesShimmer.startShimmerAnimation();
        mapShimmer.startShimmerAnimation();
        confirmLocation.startShimmerAnimation();
        adventureShimmer.startShimmerAnimation();
        barShimmer.startShimmerAnimation();
        startExploringShimmer.startShimmerAnimation();
        askAnubisShimmer.startShimmerAnimation();
    }

    private void stopShimmerAnimation() {
        claimPlaceShimmer.stopShimmerAnimation();
        sliderShimmerFrameLayout.stopShimmerAnimation();
        badgesShimmer.stopShimmerAnimation();
        socialActivitiesShimmer.stopShimmerAnimation();
        mapShimmer.stopShimmerAnimation();
        confirmLocation.stopShimmerAnimation();
        adventureShimmer.stopShimmerAnimation();
        barShimmer.stopShimmerAnimation();
        startExploringShimmer.stopShimmerAnimation();
        askAnubisShimmer.stopShimmerAnimation();
    }

    private void setLayoutVisible() {
        gamificationLayout.setVisibility(View.VISIBLE);
        shimmerLayout.setVisibility(View.GONE);
    }
}