package com.visitegypt.presentation.gamification;

import static com.visitegypt.domain.model.PlaceActivity.POST_REVIEW;
import static com.visitegypt.utils.Constants.PLACE_ID;
import static com.visitegypt.utils.GeneralUtils.LiveDataUtil.observeOnce;
import static com.visitegypt.utils.GeneralUtils.showButtonFailed;
import static com.visitegypt.utils.GeneralUtils.showButtonLoading;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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
import com.google.android.material.card.MaterialCardView;
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
import com.visitegypt.utils.GeneralUtils;
import com.visitegypt.utils.MergeObjects;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final MutableLiveData<Boolean> userInsideCircuitMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> userLocationLoaded = new MutableLiveData<>();
    private final MutableLiveData<Boolean> placeBadgesLoaded = new MutableLiveData<>();
    private MaterialCardView reviewBorder, postBorder, storyBorder, artifactsBorder, insightsBorder;
    private final MutableLiveData<Boolean> userBadgesLoaded = new MutableLiveData<>();
    private final MutableLiveData<Boolean> placeActivitiesLoaded = new MutableLiveData<>();
    private final MutableLiveData<Boolean> userPlaceActivitiesLoaded = new MutableLiveData<>();
    private MutableLiveData<Boolean> reviewActivityDone = new MutableLiveData<>();
    private MutableLiveData<Boolean> visitLocationActivityDone = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamification);

        initPlaceId(savedInstanceState);
        initPermissions();
        initViews();
        initViewModels(savedInstanceState);
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

    private void initViewModels(Bundle b) {
        gamificationViewModel = new ViewModelProvider(this).get(GamificationViewModel.class);
        gamificationViewModel.setPlaceId(placeId);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        gamificationViewModel.setPlaceId(placeId);
        gamificationViewModel.getItemsByPlaceId(placeId);

        try {
            gamificationViewModel.getPlaceDetail();
            observeOnce(gamificationViewModel.placesMutableLiveData, place -> {
                GamificationActivity.this.place = place;
                GamificationActivity.this.placeActivities = place.getPlaceActivities();
                Log.d(TAG, "initViewModels: loaded place: " + place.getTitle());
                stopShimmerAnimation();
                setLayoutVisible();
                mapView.getMapAsync(this);
                mapView.onCreate(b);
                artifactsRecyclerViewAdapter.setExploreArrayList(place.getExplores());
                initActivityLogic();
            });
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load, try again later", Toast.LENGTH_SHORT).show();
        }

        initBadges();
    }

    private void setReviewActivityComplete() {
        reviewImageButton.setOnClickListener(view ->
                Toast.makeText(GamificationActivity.this,
                        "Congrats, you've already made it",
                        Toast.LENGTH_SHORT).show());
        reviewBorder.setStrokeColor(getResources().getColor(R.color.camel));
        reviewImageButton.setOnClickListener(view ->
                Toast.makeText(GamificationActivity.this,
                        "Congrats champ, you've already made it",
                        Toast.LENGTH_SHORT).show());
        reviewXpTextView.setPaintFlags(reviewXpTextView.getPaintFlags() |
                Paint.STRIKE_THRU_TEXT_FLAG);

        //gamificationViewModel.getUserPlaceActivity();
        //gamificationViewModel.getBadgesOfUser();

    }

    private void setVisitLocationActivityDone() {
        confirmLocationButton.setText("Complete");
        confirmLocationButton.setEnabled(false);
        distanceAwayTextView.setVisibility(View.GONE);
        visitPlaceXpTextView.setPaintFlags(reviewXpTextView.getPaintFlags() |
                Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void setPostActivityComplete() {
        postBorder.setStrokeColor(Color.GREEN);
        postXpTextView.setPaintFlags(reviewXpTextView.getPaintFlags() |
                Paint.STRIKE_THRU_TEXT_FLAG);
        postPostImageButton.setOnClickListener(view -> {
            Toast.makeText(this, "You've already made your post",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private void setStoryActivityComplete() {
        storyBorder.setStrokeColor(Color.GREEN);
        storyXpTextView.setPaintFlags(reviewXpTextView.getPaintFlags() |
                Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void setChatBotComplete() {
        // TODO put green border on chatbots
    }

    private void initPlaceActivitiesViewModel() {
        Log.d(TAG, "initPlaceActivitiesViewModel: loading...");
        gamificationViewModel.getUserPlaceActivity();
        gamificationViewModel.userPlaceActivitiesMutableLiveData.observe(this, placeActivities -> {
            int totalActivities = 0;
            int doneActivities = 0;
            AtomicInteger totalXp = new AtomicInteger();
            AtomicInteger doneXp = new AtomicInteger();
            Log.d(TAG, "initPlaceActivitiesViewModel: userPlaceActivities: " + new Gson().toJson(placeActivities));
            if (place.getPlaceActivities() != null)
                Log.d(TAG, "initPlaceActivitiesViewModel: placeActivities: " + new Gson().toJson(place.getPlaceActivities()));
            else
                Log.d(TAG, "initPlaceActivitiesViewModel: no place activities");


            for (PlaceActivity placeActivity : place.getPlaceActivities()) {
                totalActivities += placeActivity.getMaxProgress();
                totalXp.addAndGet(placeActivity.getXp());
                if (placeActivities != null) {
                    for (PlaceActivity userPlaceActivity : placeActivities) {
                        if (placeActivity.getId().equals(userPlaceActivity.getId())) {
                            MergeObjects.MergeTwoObjects.merge(placeActivity, userPlaceActivity);
                            Log.d(TAG, "initPlaceActivitiesViewModel: " + new Gson().toJson(placeActivity));
                            doneActivities += placeActivity.getProgress();
                            if (placeActivity.isFinished()) {
                                doneXp.addAndGet(placeActivity.getXp());
                                switch (placeActivity.getType()) {
                                    case PlaceActivity.VISIT_LOCATION:
                                        setVisitLocationActivityDone();
                                        break;
                                    case PlaceActivity.POST_REVIEW:
                                        setReviewActivityComplete();
                                        break;
                                    case PlaceActivity.ASK_CHAT_BOT:
                                        setChatBotComplete();
                                        break;
                                    case PlaceActivity.POST_POST:
                                        setPostActivityComplete();
                                        break;
                                    case PlaceActivity.POST_STORY:
                                        setStoryActivityComplete();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
            }

            int finalTotalActivities = totalActivities;
            int finalDoneActivities = doneActivities;
            observeOnce(placeBadgesLoaded, aBoolean -> {
                if (aBoolean) {
                    for (Badge badge : placeBadges) {
                        totalXp.addAndGet(badge.getXp());
                        if (badge.isOwned())
                            doneXp.addAndGet(badge.getXp());
                    }
                    placeProgressIndicator.setMax(totalXp.get());
                    placeProgressIndicator.setProgress(doneXp.get(), true);

                    if (finalTotalActivities > finalDoneActivities && (finalTotalActivities - finalDoneActivities) > 1)
                        placeRemainingActivitiesTextView.setText(MessageFormat.format("{0} remaining activities",
                                finalTotalActivities - finalDoneActivities));
                    else if ((finalTotalActivities - finalDoneActivities) == 1) {
                        placeRemainingActivitiesTextView.setText("1 remaining activity");
                    } else {
                        placeRemainingActivitiesTextView.setText("Completed");
                    }
                    placeXpTextView.setText(MessageFormat.format("{0}/{1} XP", doneXp, totalXp));
                }
            });
            //placeBadgesLoaded.observe(this,);

            Log.d(TAG, "initPlaceActivitiesViewModel: total activities: " + totalActivities);
            Log.d(TAG, "initPlaceActivitiesViewModel: done activities: " + doneActivities);
            Log.d(TAG, "initPlaceActivitiesViewModel: total xp: " + totalXp);
            Log.d(TAG, "initPlaceActivitiesViewModel: done xp: " + doneXp);

            placeProgressIndicator.setMax(totalXp.get());
            placeProgressIndicator.setProgress(doneXp.get(), true);

            if (totalActivities > doneActivities && (totalActivities - doneActivities) > 1)
                placeRemainingActivitiesTextView.setText(MessageFormat.format("{0} remaining activities", totalActivities - doneActivities));
            else if ((totalActivities - doneActivities) == 1) {
                placeRemainingActivitiesTextView.setText("1 remaining activity");
            } else {
                placeRemainingActivitiesTextView.setText(R.string.complete);
            }
            placeXpTextView.setText(MessageFormat.format("{0}/{1} XP", doneXp, totalXp));
        });
    }

    private void initBadges() {
        gamificationViewModel.setPlaceId(placeId);
        gamificationViewModel.getBadgesOfUser();
        gamificationViewModel.getPlaceBadges();
        gamificationViewModel.placeBadgesMutableLiveData.observe(this, placeBadges -> {
            this.placeBadges = (ArrayList<Badge>) placeBadges;
            Log.d(TAG, "initBadgesViewModel: place badges: " + new Gson().toJson(placeBadges));
            //Log.d(TAG, "initViewModel: BOOM" + new Gson().toJson(placeBadges.get(1).getBadgeTasks()));
            observeOnce(gamificationViewModel.userBadgesMutableLiveData, userBadges -> {
                //Log.d(TAG, "initViewModel: BOOM" + new Gson().toJson(userBadges.get(1).getBadgeTasks()));
                Log.d(TAG, "initViewModel: ");
                for (Badge badge : userBadges) {
                    for (Badge placeBadge : placeBadges) {
                        if (badge.getId().equals(placeBadge.getId())) {
                            // same badge -> merge them
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
                placeBadgesLoaded.setValue(true);
                badgesSliderViewAdapter.setBadges((ArrayList<Badge>) placeBadges);
            });


        });
    }

    @SuppressLint("MissingPermission")
    private void initViews() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapView = findViewById(R.id.mapViewGamificationActivity);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

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
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        postCardConstraintLayout = findViewById(R.id.postCardConstraintLayout);
        postCardConstraintLayout.setBackgroundResource(R.drawable.card_review_edge);

        reviewBorder = findViewById(R.id.cardReviewBorderGamificationActivity);
        postBorder = findViewById(R.id.cardPostBorderGamificationActivity);
        storyBorder = findViewById(R.id.cardStoryBorderGamificationActivity);

        artifactsBorder = findViewById(R.id.artifactsCardGamificationActivityBorder);
        insightsBorder = findViewById(R.id.insightsCardGamificationActivityBorder);

        claimButton = findViewById(R.id.claimPlaceGamificationButton);
        claimButton.setOnClickListener(view -> GeneralUtils.showUserProgress(GamificationActivity.this, claimButton, null, null));
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
        try {
            if (place.getDefaultImage() == null) {
                Picasso.get().load(place.getImageUrls().get(0)).into(placeImageView);
            } else {
                Picasso.get().load(place.getDefaultImage()).into(placeImageView);
            }
        } catch (Exception e) {
            Log.e(TAG, "initActivityLogic: loading image failed: " + e.getMessage());
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
            Log.d(TAG, "confirmLocation: trying to confirm location...");
            for (PlaceActivity placeActivity : placeActivities) {
                if (placeActivity.getType().equals(PlaceActivity.VISIT_LOCATION)) {
                    Log.d(TAG, "confirmLocation: found the visit location place activity" + new Gson().toJson(placeActivity));
                    observeOnce(userInsideCircuitMutableLiveData, aBoolean -> {
                        if (aBoolean) {
                            updatePlaceActivityProgress(placeActivity);
                        } else {
                            GeneralUtils.showSnackError(GamificationActivity.this,
                                    mapView, "You're not inside yet");
                        }
                    });

                }
            }
        }
    }

    private void initPermissions() {
        if (ActivityCompat.checkSelfPermission(GamificationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(GamificationActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GamificationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

                PlaceActivity reviewPlaceActivity = null;
                for (PlaceActivity placeActivity : placeActivities) {
                    if (placeActivity.getType().equals(POST_REVIEW)) {
                        Log.d(TAG, "showReviewDialog: found activity review: " + new Gson().toJson(placeActivity));
                        reviewPlaceActivity = placeActivity;
                        reviewViewModel.setReviewPlaceActivity(placeActivity);
                    }
                }

                BadgeTask reviewBadgeTask = null;
                for (Badge badge : placeBadges) {
                    if (badge.getTitle().equals("Social Butterfly")) {
                        for (BadgeTask badgeTask : badge.getBadgeTasks()) {
                            if (badgeTask.getTaskTitle().equals("review the place")) {
                                badgeTask.setBadgeId(badge.getId());
                                Log.d(TAG, "showReviewDialog: found review badge task: " + new Gson().toJson(badgeTask));
                                reviewViewModel.setReviewBadgeTask(badgeTask);
                                reviewBadgeTask = badgeTask;
                            }
                        }
                    }
                }

                try {
                    reviewViewModel.submitReview(placeId, review);
                    PlaceActivity finalReviewPlaceActivity = reviewPlaceActivity;

                    ArrayList<PlaceActivity> activityArrayList = new ArrayList<>();
                    observeOnce(reviewViewModel.placeActivityUpdateResponseCode, integer -> {
                        if (integer == 200) {
                            setReviewActivityComplete();
                            activityArrayList.add(finalReviewPlaceActivity);
                            GeneralUtils.showUserProgress(GamificationActivity.this, reviewImageButton,
                                    activityArrayList, null);
                        } else {
                            GeneralUtils.showSnackError(GamificationActivity.this, reviewImageButton,
                                    integer.toString());
                        }
                    });

                    ArrayList<BadgeTask> badgeTaskArrayList = new ArrayList<>();
                    BadgeTask finalReviewBadgeTask = reviewBadgeTask;
                    observeOnce(reviewViewModel.badgeTaskUpdateResponseCode, integer -> {
                        if (integer == 200) {
                            setReviewActivityComplete();
                            addReviewDialog.dismiss();

                            badgeTaskArrayList.add(finalReviewBadgeTask);
                            GeneralUtils.showUserProgress(this, reviewImageButton,
                                    null, badgeTaskArrayList);
                        } else {
                            //GeneralUtils.showSnackError(this, reviewImageButton, integer.toString());
                            Toast.makeText(this, "Failed to post review", Toast.LENGTH_SHORT).show();
                        }
                    });

                    observeOnce(reviewViewModel.mutableLiveDataResponseCode, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            if (integer == 200) {
                                GeneralUtils.showButtonLoaded(submitReviewButton, null);
                                if (addReviewDialog != null) {
                                    addReviewDialog.dismiss();
                                    Toast.makeText(GamificationActivity.this, "review submitted", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                showButtonFailed(submitReviewButton, null, "submit");
                            }
                        }
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