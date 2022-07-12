package com.visitegypt.presentation.gamification;

import static com.visitegypt.utils.GeneralUtils.LiveDataUtil.observeOnce;
import static com.visitegypt.utils.GeneralUtils.showButtonFailed;
import static com.visitegypt.utils.GeneralUtils.showButtonLoaded;
import static com.visitegypt.utils.GeneralUtils.showButtonLoading;
import static com.visitegypt.utils.GeneralUtils.showButtonLoadingRight;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.razir.progressbutton.ButtonTextAnimatorExtensionsKt;
import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressButtonHolderKt;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.Explore;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.model.FullExplore;
import com.visitegypt.domain.model.FullPlaceActivity;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.XPUpdate;
import com.visitegypt.presentation.chatbot.ChatbotActivity;
import com.visitegypt.presentation.home.HomeRecyclerViewAdapter;
import com.visitegypt.presentation.post.PostActivity;
import com.visitegypt.presentation.review.ReviewViewModel;
import com.visitegypt.utils.Constants;
import com.visitegypt.utils.GamificationRules;
import com.visitegypt.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import kotlin.Unit;
import se.warting.permissionsui.backgroundlocation.PermissionsUiContracts;

@AndroidEntryPoint
public class GamificationActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    public static final String MSG_TYPE = "type";
    public static final String ARTIFACTS = "artifacts";
    public static final String INSIGHTS = "insights";
    public static final String PLACE_TITLE = "placeTitle";
    public static final String PLACE_ID = "placeId";
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
    private MaterialButton claimButton, confirmLocationButton;
    private ArtifactsRecyclerViewAdapter artifactsRecyclerViewAdapter;
    private RecyclerView artifactsRecyclerView;
    private MapView mapView;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private double latitude, longitude;
    private GamificationViewModel gamificationViewModel;
    private Place place;

    private ShimmerFrameLayout sliderShimmerFrameLayout, claimPlaceShimmer, badgesShimmer;
    @SuppressLint("SetTextI18n")
    ActivityResultLauncher<Unit> mGetContent = registerForActivityResult(
            new PermissionsUiContracts.RequestBackgroundLocation(),
            success -> {
                if (success) {
                    Log.d(TAG, "set permission granted");
                } else {
                    GeneralUtils.showSnackError(this, mapView, "Permission set error");
                }
            });
    private ImageView placeImageView;
    private TextView placeTitleTextView, placeRemainingActivitiesTextView, placeXpTextView;
    private ImageButton reviewImageButton, postPostImageButton, postStoryImageButton;
    private TextView visitPlaceXpTextView;
    private TextView storyXpTextView, postXpTextView, reviewXpTextView;
    private TextView insightsXpTextView, artifactsXpTextView;
    private TextView distanceAwayTextView;
    private ImageButton askAboutInsightsImageButton, askAboutArtifactsImageButton;
    private LinearProgressIndicator placeProgressIndicator, adventureLinearProgressIndicator;
    private LinearLayout shimmerLayout, gamificationLayout;
    private ConstraintLayout postCardConstraintLayout;
    private final MutableLiveData<Boolean> userInsideCircuitMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> userLocationLoaded = new MutableLiveData<>();
    private User user;

    private MaterialCardView reviewBorder, postBorder, storyBorder, artifactsBorder, insightsBorder;
    private int timeout = 0;
    private ShimmerFrameLayout socialActivitiesShimmer, mapShimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamification);

        initPermissions();
        initPlaceId(savedInstanceState);
        initViews();
        initViewModels(savedInstanceState);
        initClickListeners();
        dummyTests();
    }

    private void dummyTests() {
        GeneralUtils.showUserProgress(this,
                confirmLocationButton,
                null,
                null,
                new XPUpdate(35, 55),
                sharedPreferences.getString(Constants.SHARED_PREF_USER_IMAGE, ""));
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

    private void initViewModels(Bundle b) {
        gamificationViewModel = new ViewModelProvider(this).get(GamificationViewModel.class);
        gamificationViewModel.setPlaceId(placeId);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        gamificationViewModel.setPlaceId(placeId);
        gamificationViewModel.getItemsByPlaceId(placeId);

        gamificationViewModel.getUser();
        observeOnce(gamificationViewModel.userMutableLiveData, user1 -> {
            user = user1;
        });

        try {
            gamificationViewModel.getPlaceDetail();
            observeOnce(gamificationViewModel.placesMutableLiveData, place -> {
                if (place.getExplores() != null)
                    for (Explore explore : place.getExplores()) {
                        // explore.setXp(GamificationRules.EXPLORE_XP);
                        // explore.setMaxProgress(1);
                        place.getPlaceActivities().add(explore);
                    }
                GamificationActivity.this.place = place;
                GamificationActivity.this.placeActivities = place.getPlaceActivities();
                Log.d(TAG, "initViewModels: loaded place: " + place.getTitle());
                stopShimmerAnimation();
                setLayoutVisible();
                mapView.getMapAsync(this);
                mapView.onCreate(b);
                //artifactsRecyclerViewAdapter.setExploreArrayList(place.getExplores());
                getExplores();
                initActivityLogic();
            });
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load, try again later", Toast.LENGTH_SHORT).show();
        }
    }

    private void setReviewActivityComplete() {
        reviewImageButton.setOnClickListener(view ->
                Toast.makeText(GamificationActivity.this,
                        "Congrats, you've already made it",
                        Toast.LENGTH_SHORT).show());
        reviewBorder.setStrokeColor(getResources().getColor(R.color.camel));
//        reviewImageButton.setOnClickListener(view ->
//                Toast.makeText(GamificationActivity.this,
//                        "Congrats champ, you've already made it",
//                        Toast.LENGTH_SHORT).show());
        reviewXpTextView.setPaintFlags(reviewXpTextView.getPaintFlags() |
                Paint.STRIKE_THRU_TEXT_FLAG);

    }

    private void setVisitLocationActivityDone() {
        confirmLocationButton.setText("Complete");
        confirmLocationButton.setEnabled(false);
        confirmLocationButton.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.camel)));
        distanceAwayTextView.setVisibility(View.GONE);
        visitPlaceXpTextView.setPaintFlags(reviewXpTextView.getPaintFlags() |
                Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void setPostActivityComplete() {
        postBorder.setStrokeColor(getResources().getColor(R.color.camel));
        postXpTextView.setPaintFlags(reviewXpTextView.getPaintFlags() |
                Paint.STRIKE_THRU_TEXT_FLAG);
        postPostImageButton.setOnClickListener(view -> {
            Toast.makeText(this, "You've already made your post",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void setStoryActivityComplete() {
        storyBorder.setStrokeColor(Color.YELLOW);
        storyXpTextView.setPaintFlags(reviewXpTextView.getPaintFlags() |
                Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void setChatBotPlaceComplete() {
        insightsBorder.setStrokeColor(getResources().getColor(R.color.camel));
        insightsXpTextView.setPaintFlags(reviewXpTextView.getPaintFlags() |
                Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void setChatBotArtifactsComplete() {
        artifactsBorder.setStrokeColor(getResources().getColor(R.color.camel));
        artifactsXpTextView.setPaintFlags(reviewXpTextView.getPaintFlags() |
                Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @SuppressLint("MissingPermission")
    private void initViews() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapView = findViewById(R.id.mapViewGamificationActivity);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

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
        shimmerLayout = findViewById(R.id.shimmerLayout);
        gamificationLayout = findViewById(R.id.gamificationLayout);
        startShimmerAnimation();
        /*******************************************************************/
        claimButton = findViewById(R.id.startExploringGamificationActivityButton);
        confirmLocationButton = findViewById(R.id.confirmLocationGamificationActivityButton);

        ProgressButtonHolderKt.bindProgressButton(this, confirmLocationButton);
        ButtonTextAnimatorExtensionsKt.attachTextChangeAnimator(confirmLocationButton);
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

        postCardConstraintLayout = findViewById(R.id.postCardConstraintLayout);
        postCardConstraintLayout.setBackgroundResource(R.drawable.card_review_edge);

        reviewBorder = findViewById(R.id.cardReviewBorderGamificationActivity);
        postBorder = findViewById(R.id.cardPostBorderGamificationActivity);
        storyBorder = findViewById(R.id.cardStoryBorderGamificationActivity);

        artifactsBorder = findViewById(R.id.artifactsCardGamificationActivityBorder);
        insightsBorder = findViewById(R.id.insightsCardGamificationActivityBorder);
        adventureLinearProgressIndicator = findViewById(R.id.adventureGamificationActivityProgress);
    }


    private void showPostPost() {
        Intent intent = new Intent(this, PostActivity.class);
        Log.d(TAG, "showPostPost: this is show post place id" + placeId);
        intent.putExtra(PLACE_ID, placeId);
        startActivity(intent);
    }

    private void showPostStory() {
        Toast.makeText(this, "Stay tuned, coming soon...", Toast.LENGTH_SHORT).show();
    }

    private void showChatBotArtifacts() {
        Intent intent = new Intent(this, ChatbotActivity.class);
        intent.putExtra(PLACE_TITLE, place.getTitle());
        intent.putExtra(MSG_TYPE, ARTIFACTS);
        intent.putExtra(PLACE_ID, place.getId());
        startActivity(intent);
    }

    private void showChatBotPlace() {
        Intent intent = new Intent(this, ChatbotActivity.class);
        intent.putExtra(PLACE_TITLE, place.getTitle());
        intent.putExtra(MSG_TYPE, INSIGHTS);
        intent.putExtra(PLACE_ID, place.getId());
        startActivity(intent);
    }

    private void initActivityLogic() {
        placeTitleTextView.setText(place.getTitle());
        try {
            if (place.getDefaultImage() == null) {
                Picasso.get().load(place.getImageUrls().get(0)).into(placeImageView);
            } else {
                Picasso.get().load(place.getDefaultImage()).into(placeImageView);
            }
        } catch (Exception e) {
            Log.e(TAG, "initActivityLogic: loading image failed: " + e.getMessage());
        }
        initActivities();
        initViewModelObservers();
    }

    private void checkActivityComplete(FullPlaceActivity fullPlaceActivity) {
        if (fullPlaceActivity.isFinished()) {
            if (fullPlaceActivity.getPlaceActivity().getType() != null) {
                switch (fullPlaceActivity.getPlaceActivity().getType()) {
                    case PlaceActivity.VISIT_LOCATION:
                        setVisitLocationActivityDone();
                        break;
                    case PlaceActivity.POST_REVIEW:
                        setReviewActivityComplete();
                        break;
                    case PlaceActivity.POST_POST:
                        setPostActivityComplete();
                        break;
                    case PlaceActivity.ASK_CHAT_BOT_PLACE:
                        setChatBotPlaceComplete();
                        break;
                    case PlaceActivity.ASK_CHAT_BOT_ARTIFACTS:
                        setChatBotArtifactsComplete();
                        break;
                    default:
                        break;
                }
            } else {
                // TODO explores
            }
        }
    }

    private void initViewModelObservers() {
        gamificationViewModel.fullPlaceActivitiesMutableLiveData.observe(this, fullPlaceActivities -> {
            Log.d(TAG, "initActivities: " + new Gson().toJson(fullPlaceActivities));
            int progress = 0, maxProgress = 0, xp = 0, maxXp = 0;
            if (fullPlaceActivities != null)
                for (FullPlaceActivity fullPlaceActivity : fullPlaceActivities) {
                    progress += fullPlaceActivity.getProgress();
                    xp += fullPlaceActivity.isFinished() ? fullPlaceActivity.getPlaceActivity().getXp() : 0;
                    checkActivityComplete(fullPlaceActivity);
                }
            if (place.getPlaceActivities() != null)
                for (PlaceActivity placeActivity : place.getPlaceActivities()) {
                    maxProgress += placeActivity.getMaxProgress();
                    maxXp += placeActivity.getXp();
                }

            int remaining = maxProgress - progress;
            placeXpTextView.setText((maxXp - xp) + "XP remaining");
            if (remaining == 1) {
                placeRemainingActivitiesTextView.setText("1 remaining activity");
            } else if (remaining == 0) {
                placeXpTextView.setText(maxXp + "XP Earned");
                placeRemainingActivitiesTextView.setText("all activities complete");
            } else {
                placeRemainingActivitiesTextView.setText(progress + "/" + maxProgress + " remaining activities");
            }
            placeProgressIndicator.setMax(maxProgress);
            placeProgressIndicator.setProgress(progress, true);
        });

        gamificationViewModel.fullBadgesMutableLiveData.observe(this, fullBadges -> {
            Log.d(TAG, "initActivities place badges: " + new Gson().toJson(fullBadges));
            observeOnce(gamificationViewModel.placeBadgesMutableLiveData, badges -> {
                if (fullBadges != null && badges != null) {
                    for (FullBadge fullBadge : fullBadges) {
                        for (Badge placeBadge : badges) {
                            if (fullBadge.getBadge().getId().equals(placeBadge.getId())) {
                                Log.d(TAG, "initActivities: " + placeBadge.getId() + " matched");
                                Log.d(TAG, "initActivities: " + new Gson().toJson(placeBadge.getBadgeTasks()));
                                Log.d(TAG, "initActivities: " + new Gson().toJson(fullBadge.getBadgeTasks()));

                                for (BadgeTask placeBadgeTask : placeBadge.getBadgeTasks()) {
                                    for (BadgeTask fullBadgeTask : fullBadge.getBadgeTasks()) {
                                        if (placeBadgeTask.getTaskTitle().equals(fullBadgeTask.getTaskTitle())) {
                                            placeBadgeTask.setProgress(fullBadgeTask.getProgress());
                                        }
                                    }
                                }
                                placeBadge.setProgress(fullBadge.getProgress());
                                placeBadge.setOwned(fullBadge.isOwned());
                                //placeBadge.setBadgeTasks(fullBadge.getBadge().getBadgeTasks());
                            }
                        }
                    }
                }
                badgesSliderViewAdapter.setBadges((ArrayList<Badge>) badges);
            });
        });
    }

    private void initActivities() {
        gamificationViewModel.getFullPlaceActivities();
        gamificationViewModel.getFullBadges();
        gamificationViewModel.getFullPlaceBadges();
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
            showChatBotArtifacts();
        });
        askAboutInsightsImageButton.setOnClickListener(view -> {
            showChatBotPlace();
        });
    }

    private void getBadgesAndActivities() {
        gamificationViewModel.getFullPlaceActivities();
        gamificationViewModel.getFullBadges();
    }

    private void confirmLocation() {
        showButtonLoadingRight(confirmLocationButton);
        observeOnce(userInsideCircuitMutableLiveData, userInside -> {
            if (userInside) {
                gamificationViewModel.finishVisitLocation();
                observeOnce(gamificationViewModel.updatedVisitLocationMutableLiveData, xpUpdate -> {
                    if (xpUpdate != null) {
                        showButtonLoaded(confirmLocationButton, "Complete");
                        confirmLocationButton.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.camel)));
                        setVisitLocationActivityDone();
                        updateUserXP(xpUpdate);
                        // observeOnce(gamificationViewModel.userMutableLiveData, this::updateUserXP);
                    } else {
                        showButtonFailed(confirmLocationButton, "failed to update location", "confirm location");
                    }
                    getBadgesAndActivities();
                });

            } else {
                showButtonFailed(confirmLocationButton, "You're not inside yet",
                        "confirm location");
                //initActivities();
                getBadgesAndActivities();
                Log.e(TAG, "confirmLocation: " + "failed to confirm location");
            }
        });
    }

    private void initPermissions() {
        if (ActivityCompat.checkSelfPermission(GamificationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(GamificationActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mGetContent.launch(null);
//            ActivityCompat.requestPermissions(GamificationActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

        Button submitReviewButton = addReviewDialog.findViewById(R.id.submitReviewButton);
        ProgressButtonHolderKt.bindProgressButton(this, submitReviewButton);
        ButtonTextAnimatorExtensionsKt.attachTextChangeAnimator(submitReviewButton);

        submitReviewButton.setOnClickListener(view -> {
            showButtonLoading(submitReviewButton);
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

                try {
                    reviewViewModel.submitReview(placeId, review);
                    observeOnce(reviewViewModel.activityUpdatedMutableLiveData, xpUpdate -> {
                        if (xpUpdate != null) {
                            Log.d(TAG, "showReviewDialog: update review successful");
                            setReviewActivityComplete();
                            Log.d(TAG, "updateUserXP: old XP " + user.getXp());
                            gamificationViewModel.getUser();
                            updateUserXP(xpUpdate);
                            addReviewDialog.dismiss();
                            getBadgesAndActivities();
                        } else {
                            showButtonFailed(submitReviewButton, null, "submit");
                        }
                        // initActivities();
                    });
                } catch (Exception e) {
                    Log.e(TAG, "showReviewDialog: " + e.getMessage());
                    Toast.makeText(this, "Failed to post review", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    showButtonFailed(submitReviewButton, null, "submit");
                    DrawableButtonExtensionsKt.hideProgress(submitReviewButton, "boom");
                }
            }
        });
    }

    private void updateUserXP(XPUpdate xpUpdate) {
        String imageUrl = sharedPreferences.getString(Constants.SHARED_PREF_USER_IMAGE, "");
        GeneralUtils.showUserProgress(this,
                confirmLocationButton,
                null,
                null,
                xpUpdate,
                imageUrl);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null && place != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
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

                    distance[0] = 100;
                    if (distance[0] > GamificationRules.CONFIRM_LOCATION_CIRCLE_RADIUS) {
                        // confirmLocation();
                        distanceAwayTextView.setText(String.format(Locale.CANADA, "You are %,.2f metres away!", distance[0]));
                        userInsideCircuitMutableLiveData.setValue(false);
                    } else if (distance[0] > GamificationRules.CONFIRM_LOCATION_CIRCLE_RADIUS / 1000f &&
                            GamificationRules.CONFIRM_LOCATION_CIRCLE_RADIUS < distance[0]) {
                        distanceAwayTextView.setText(String.format(Locale.CANADA, "You are only %,.0f metres away!", distance[0]));
                        userInsideCircuitMutableLiveData.setValue(false);
                    } else {
                        distanceAwayTextView.setText(String.format(Locale.CANADA, "You are inside, you may confirm now!", distance[0]));
                        userInsideCircuitMutableLiveData.setValue(true);
                        //confirmLocation();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mGetContent.launch(null);

//            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//            ActivityCompat.requestPermissions(this, permissions, 148914);
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            Log.d(TAG, "onResume: set location listener success");

        } catch (Exception ignored) {
            Log.e(TAG, "onResume: set location listener failed");
        }
        if (mapView != null && place != null) {
            Log.d(TAG, "onResume: map resumed");
            mapView.onResume();
        }
        try {
            if (gamificationViewModel != null && place != null) {
                // initActivities();
                getBadgesAndActivities();
                getExplores();
            }
        } catch (Exception ignored) {

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
        if (mapView != null) {
            try {
                mapView.onPause();
            } catch (Exception ignored) {
            }
        }
        stopShimmerAnimation();
        locationManager.removeUpdates(this);

    }

    private void startShimmerAnimation() {
        claimPlaceShimmer.startShimmerAnimation();
        sliderShimmerFrameLayout.startShimmerAnimation();
        badgesShimmer.startShimmerAnimation();
        socialActivitiesShimmer.startShimmerAnimation();
        mapShimmer.startShimmerAnimation();
    }

    private void stopShimmerAnimation() {
        claimPlaceShimmer.stopShimmerAnimation();
        sliderShimmerFrameLayout.stopShimmerAnimation();
        badgesShimmer.stopShimmerAnimation();
        socialActivitiesShimmer.stopShimmerAnimation();
        mapShimmer.stopShimmerAnimation();
    }

    private void setLayoutVisible() {
        gamificationLayout.setVisibility(View.VISIBLE);
        shimmerLayout.setVisibility(View.GONE);
    }

    public void getExplores() {
        gamificationViewModel.getFullExplores();
        artifactsRecyclerViewAdapter.setExploreArrayList(place.getExplores());
        if (place.getExplores() != null)
            observeOnce(gamificationViewModel.fullExploreMutableLiveData, fullExplores -> {
                int progress = 0, maxProgress = place.getExplores().size();
                if (fullExplores != null)
                    for (FullExplore fullExplore : fullExplores) {
                        for (Explore explore : place.getExplores()) {
                            if (explore.getId().equals(fullExplore.getId())) {
                                explore.setProgress(fullExplore.getProgress());
                                progress += fullExplore.getProgress();
                            }
                        }
                    }
                adventureLinearProgressIndicator.setMax(maxProgress);
                adventureLinearProgressIndicator.setProgress(progress, true);
                artifactsRecyclerViewAdapter.setExploreArrayList(place.getExplores());
            });

    }
}