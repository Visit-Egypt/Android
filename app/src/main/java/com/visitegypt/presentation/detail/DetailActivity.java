package com.visitegypt.presentation.detail;

import static com.visitegypt.utils.Constants.CustomerType.CHILDREN;
import static com.visitegypt.utils.Constants.CustomerType.EGYPTIAN_ADULT;
import static com.visitegypt.utils.Constants.CustomerType.EGYPTIAN_PHOTO;
import static com.visitegypt.utils.Constants.CustomerType.EGYPTIAN_STUDENT;
import static com.visitegypt.utils.Constants.CustomerType.EGYPTIAN_VIDEO;
import static com.visitegypt.utils.Constants.CustomerType.FOREIGNER_ADULT;
import static com.visitegypt.utils.Constants.CustomerType.FOREIGNER_ADULT_PHOTO;
import static com.visitegypt.utils.Constants.CustomerType.FOREIGNER_ADULT_VIDEO;
import static com.visitegypt.utils.Constants.CustomerType.FOREIGNER_STUDENT;
import static com.visitegypt.utils.Constants.Days.FRIDAY;
import static com.visitegypt.utils.Constants.Days.MONDAY;
import static com.visitegypt.utils.Constants.Days.SATURDAY;
import static com.visitegypt.utils.Constants.Days.SUNDAY;
import static com.visitegypt.utils.Constants.Days.THURSDAY;
import static com.visitegypt.utils.Constants.Days.TUESDAY;
import static com.visitegypt.utils.Constants.Days.WEDNESDAY;
import static com.visitegypt.utils.GeneralUtils.LiveDataUtil.observeOnce;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.smarteist.autoimageslider.SliderView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Explore;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.Slider;
import com.visitegypt.presentation.chatbot.ChatbotActivity;
import com.visitegypt.presentation.detail.paging.ItemComparator;
import com.visitegypt.presentation.detail.paging.ItemPagingAdapter;
import com.visitegypt.presentation.gamification.GamificationActivity;
import com.visitegypt.presentation.home.HomeRecyclerViewAdapter;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.review.ReviewActivity;
import com.visitegypt.presentation.review.ReviewViewModel;
import com.visitegypt.utils.Constants;
import com.visitegypt.utils.GamificationRules;
import com.visitegypt.utils.GeneralUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "Detail Activity";

    @Inject
    public SharedPreferences sharedPreferences;
    public String placeIdToReview, placeIdFromReview;
    Handler mHandler;
    private final Runnable m_Runnable = new Runnable() {
        public void run() {
//            Toast.makeText(DetailActivity.this,"in runnable",Toast.LENGTH_SHORT).show();

            DetailActivity.this.mHandler.postDelayed(m_Runnable, 5000);
        }

    };
    private NestedScrollView detailNestedScrollView;
    private TextView foreignerAdultPriceTextView, foreignerStudentPriceTextView,
            egyptianStudentTextView, egyptianAdultPriceTextView, descriptionTextView,
            placeTitleTextView, saturdayOpeningHours, sundayOpeningHours, mondayOpeningHours,
            tuesdayOpeningHours, thursdayOpeningHours, wednesdayOpeningHours,
            fridayOpeningHours, foreignerVideoPriceTextView, foreignerPhotoPriceTextView,
            egyptianVideoPriceTextView, egyptianPhotoPriceTextView, childrenPriceTextView,
            locationTextView, remainingActivitiesTextView;
    private LinearProgressIndicator remainingActivitiesProgressIndicator;
    private MaterialButton addReviewButton, gamificationInDetailActivityImageView, seeAllButton;
    private FloatingActionButton chatbotFloatingActionButton;
    private DetailViewModel detailViewModel;
    private ReviewViewModel reviewViewModel;
    //    private ItemsRecyclerViewAdapter itemsRecyclerViewAdapter;
    private RecyclerView itemsRecyclerView;
    private SliderView sliderView;
    private SliderAdapter sliderAdapter;
    private ArrayList<Slider> sliderArrayList;
    private Dialog addReviewDialog;
    private String placeId;
    private ShimmerFrameLayout sliderShimmerFrameLayout, titleShimmerFrameLayout,
            descriptionShimmerFrameLayout, artifactsShimmerFrameLayout;
    private ItemPagingAdapter itemPagingAdapter;
    private LinearLayout detailLayout, firstReviewLinearLayout;
    private ScrollView shimmerScrollView;
    private CircularImageView backArrowCircularImageButton;
    private MapView mapView;
    private Place place;
    private MaterialTextView noReviewsMaterialTextView, visitorNameFirstReviewTextView, reviewContentTextView;
    private RatingBar firstReviewRatingBar;
    private LinearLayoutCompat artifactsLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.mHandler = new Handler();
        m_Runnable.run();

        placeIdFromReview = getIntent().getStringExtra("place_id");

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
        if (placeId == null) {
            placeIdToReview = placeIdFromReview;
            placeId = placeIdFromReview;
        } else {
            placeIdToReview = placeId;
        }

        initViews();
        if (placeIdFromReview != null) {
            initViewModel(placeIdFromReview, savedInstanceState);
        } else {
            initViewModel(placeId, savedInstanceState);
        }
        chatBot();
        getAllItems();
    }

    private void initViews() {
        foreignerAdultPriceTextView = findViewById(R.id.foreignerAdultPriceTextView);
        foreignerStudentPriceTextView = findViewById(R.id.foreignerStudentPriceTextView);
        egyptianStudentTextView = findViewById(R.id.egyptianStudentTextView);
        egyptianAdultPriceTextView = findViewById(R.id.egyptianAdultPriceTextView);
        foreignerVideoPriceTextView = findViewById(R.id.foreignerVideoPriceTextView);
        foreignerPhotoPriceTextView = findViewById(R.id.foreignerPhotoPriceTextView);
        egyptianVideoPriceTextView = findViewById(R.id.egyptianVideoPriceTextView);
        egyptianPhotoPriceTextView = findViewById(R.id.egyptianPhotoPriceTextView);
        childrenPriceTextView = findViewById(R.id.childrenPriceTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        placeTitleTextView = findViewById(R.id.placeTitleTextView);
        backArrowCircularImageButton = findViewById(R.id.backArrowCircularImageButton);
        gamificationInDetailActivityImageView = findViewById(R.id.gamificationInDetailActivityImageView);

        saturdayOpeningHours = findViewById(R.id.saturdayOpeningHoursTextView);
        sundayOpeningHours = findViewById(R.id.sundayOpeningHoursTextView);
        mondayOpeningHours = findViewById(R.id.mondayOpeningHoursTextView);
        tuesdayOpeningHours = findViewById(R.id.tuesdayOpeningHoursTextView);
        wednesdayOpeningHours = findViewById(R.id.wednesdayOpeningHoursTextView);
        thursdayOpeningHours = findViewById(R.id.thursdayOpeningHoursTextView);
        fridayOpeningHours = findViewById(R.id.fridayOpeningHoursTextView);

        chatbotFloatingActionButton = findViewById(R.id.chatbotFloatingActionButton);
        addReviewButton = findViewById(R.id.writeReviewButton);
        detailNestedScrollView = findViewById(R.id.detailNestedScrollView);

        sliderView = findViewById(R.id.sliderSliderView);
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemPagingAdapter = new ItemPagingAdapter(new ItemComparator());
        itemPagingAdapter.setContext(this);
//        itemsRecyclerViewAdapter = new ItemsRecyclerViewAdapter(this);
//        itemsRecyclerView.setAdapter(itemsRecyclerViewAdapter);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this,
                LinearLayoutManager.HORIZONTAL, true));
        itemsRecyclerView.setAdapter(itemPagingAdapter);
        /********************************************************************************/

        sliderShimmerFrameLayout = findViewById(R.id.sliderShimmerFrameLayout);
        titleShimmerFrameLayout = findViewById(R.id.titleShimmerFrameLayout);
        descriptionShimmerFrameLayout = findViewById(R.id.descriptionShimmerFrameLayout);
        artifactsShimmerFrameLayout = findViewById(R.id.artifactsShimmerFrameLayout);

        detailLayout = findViewById(R.id.detailActivityLayout);
        shimmerScrollView = findViewById(R.id.shimmerScrollView);
        sliderArrayList = new ArrayList<>();
        sliderAdapter = new SliderAdapter(sliderArrayList);

        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        mapView = findViewById(R.id.detailActivityMapView);
        mapView.getMapAsync(this);

        remainingActivitiesTextView = findViewById(R.id.remainingActivitiesDetailActivityTextView);
        remainingActivitiesProgressIndicator = findViewById(R.id.remainingActivitiesDetailActivityLinearProgressIndicator);

        noReviewsMaterialTextView = findViewById(R.id.noReviewsMaterialTextView);
        visitorNameFirstReviewTextView = findViewById(R.id.visitorNameFirstReviewTextView);
        firstReviewRatingBar = findViewById(R.id.firstReviewRatingBar);
        reviewContentTextView = findViewById(R.id.reviewContentTextView);
        firstReviewLinearLayout = findViewById(R.id.firstReviewLinearLayout);
        seeAllButton = findViewById(R.id.seeAllButton);

        // TODO TO HENDY: HIDE THIS LAYOUT WHENEVER THE PAGINATION MAGIC OF THE ITEMS IS EMPTY
        artifactsLinearLayout = findViewById(R.id.artifactsLinearLayoutDetailActivity);
    }

    private void initViewModel(String placeId, Bundle savedInstances) {
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        detailViewModel.getPlace(placeId);
        detailViewModel.placesMutableLiveData.observe(this, place -> {
            if (place.getReviews() != null && !place.getReviews().isEmpty()) {
                noReviewsMaterialTextView.setVisibility(View.GONE);
                Log.d(TAG, "reviews: " + place.getReviews().toString());
                firstReviewRatingBar.setRating(place.getReviews().get(0).getRating());
                reviewContentTextView.setText(place.getReviews().get(0).getReview());
                visitorNameFirstReviewTextView.setText(place.getReviews().get(0).getFullName());

            } else {
                firstReviewLinearLayout.setVisibility(View.GONE);
                seeAllButton.setVisibility(View.GONE);
                Log.d(TAG, "no reviews available ");
            }
        });
        detailViewModel.getItemsByPlaceId(placeId);
        backArrowCircularImageButton.setOnClickListener(v -> backPlace());

        gamificationInDetailActivityImageView.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this, GamificationActivity.class);
            intent.putExtra(HomeRecyclerViewAdapter.CHOSEN_PLACE_ID, placeId);
            startActivity(intent);
        });
        detailNestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    if (scrollY > oldScrollY + 12 && chatbotFloatingActionButton.isShown()) {
                        chatbotFloatingActionButton.hide();
                    }

                    if (scrollY < oldScrollY - 12 && !chatbotFloatingActionButton.isShown()) {
                        chatbotFloatingActionButton.show();
                    }

                    if (scrollY == 0) {
                        chatbotFloatingActionButton.show();
                    }
                }
        );

        detailViewModel.placesMutableLiveData.observe(this, place -> {

            DetailActivity.this.place = place;
            stopShimmerAnimation();
            setLayoutVisible();
            setShimmersGone();

            if (place.getPlaceActivities() == null) {
                ArrayList<PlaceActivity> placeActivities = new ArrayList<>();
                place.setPlaceActivities(placeActivities);
            }

            if (place.getExplores() != null) {
                for (Explore explore : place.getExplores()) {
                    place.getPlaceActivities().add(explore);
                }
            }

            mapView.getMapAsync(DetailActivity.this);
            mapView.onCreate(savedInstances);

            if (place.getImageUrls() != null && !place.getImageUrls().isEmpty()) {
                for (String url : place.getImageUrls()) {
                    sliderArrayList.add(new Slider(url));
                }
                sliderAdapter.updateArrayList(sliderArrayList);
            } else if (place.getDefaultImage() != null && !place.getDefaultImage().isEmpty()) {
                sliderArrayList.add(new Slider(place.getDefaultImage()));
            } else {
                Log.d(TAG, "no images found");
            }
            if (place.getTicketPrices() != null) {
                try {
                    foreignerAdultPriceTextView.setText(place.getTicketPrices().get(FOREIGNER_ADULT.toString()).toString());
                    egyptianAdultPriceTextView.setText(place.getTicketPrices().get(EGYPTIAN_ADULT.toString()).toString());
                    egyptianStudentTextView.setText(place.getTicketPrices().get(EGYPTIAN_STUDENT.toString()).toString());
                    foreignerStudentPriceTextView.setText(place.getTicketPrices().get(FOREIGNER_STUDENT.toString()).toString());
                    foreignerPhotoPriceTextView.setText(place.getTicketPrices().get(FOREIGNER_ADULT_PHOTO.toString()).toString());
                    foreignerVideoPriceTextView.setText(place.getTicketPrices().get(FOREIGNER_ADULT_VIDEO.toString()).toString());
                    egyptianVideoPriceTextView.setText(place.getTicketPrices().get(EGYPTIAN_VIDEO.toString()).toString());
                    egyptianPhotoPriceTextView.setText(place.getTicketPrices().get(EGYPTIAN_PHOTO.toString()).toString());
                    if (place.getTicketPrices().get(CHILDREN.toString()) == 0) {
                        childrenPriceTextView.setText("FREE");
                    } else {
                        childrenPriceTextView.setText(place.getTicketPrices().get(CHILDREN.toString()).toString());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "setting ticket prices failed: " + e.getMessage());
                }
            }
            if (place.getOpeningHours() != null) {
                try {
                    saturdayOpeningHours.setText(place.getOpeningHours().get(SATURDAY));
                    sundayOpeningHours.setText(place.getOpeningHours().get(SUNDAY));
                    mondayOpeningHours.setText(place.getOpeningHours().get(MONDAY));
                    tuesdayOpeningHours.setText(place.getOpeningHours().get(TUESDAY));
                    wednesdayOpeningHours.setText(place.getOpeningHours().get(WEDNESDAY));
                    thursdayOpeningHours.setText(place.getOpeningHours().get(THURSDAY));
                    fridayOpeningHours.setText(place.getOpeningHours().get(FRIDAY));
                } catch (Exception e) {
                    Log.e(TAG, "setting opening hours failed: " + e.getMessage());
                }
            }
            placeTitleTextView.setText(place.getTitle());
            descriptionTextView.setText(place.getLongDescription());

            detailViewModel.getUserPlaceActivity();
            detailViewModel.userPlaceActivitiesMutableLiveData.observe(this, placeActivities -> {

                if (placeActivities != null) {
                    GamificationRules.mergeTwoPlaceActivities(place.getPlaceActivities(),
                            placeActivities);
                }
                int progress = place.getProgress();
                Log.d(TAG, "initViewModel: progress: " + progress);
                int maxProgress = place.getMaxProgress();
                Log.d(TAG, "initViewModel: maxProgress: " + maxProgress);

                int remaining = maxProgress - progress;

                remainingActivitiesProgressIndicator.setMax(maxProgress);
                remainingActivitiesProgressIndicator.setProgress(progress, true);

                if (remaining == 0) {
                    remainingActivitiesTextView.setText("Complete");
                } else if (remaining == 1) {
                    remainingActivitiesTextView.setText("1 remaining activity");
                } else {
                    remainingActivitiesTextView.setText(place.getMaxProgress() - place.getProgress() + " remaining activities");
                }
            });

        });
    }

    private void startShimmerAnimation() {
        sliderShimmerFrameLayout.startShimmerAnimation();
        titleShimmerFrameLayout.startShimmerAnimation();
        descriptionShimmerFrameLayout.startShimmerAnimation();
        artifactsShimmerFrameLayout.startShimmerAnimation();

    }

    private void stopShimmerAnimation() {
        sliderShimmerFrameLayout.stopShimmerAnimation();
        titleShimmerFrameLayout.stopShimmerAnimation();
        descriptionShimmerFrameLayout.stopShimmerAnimation();
        artifactsShimmerFrameLayout.stopShimmerAnimation();

    }

    private void setLayoutVisible() {
        detailLayout.setVisibility(View.VISIBLE);
    }

    private void setShimmersGone() {
        shimmerScrollView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startShimmerAnimation();
        if (mapView != null && place != null) {
            mapView.onResume();
        }
        if (detailViewModel != null) {
            try {
                detailViewModel.getUserPlaceActivity();
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopShimmerAnimation();
        if (mapView != null) {
            try {
                mapView.onPause();
            } catch (Exception ignored) {
            }
        }
        mHandler.removeCallbacks(m_Runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapView != null) {
            try {
                mapView.onStop();
            } catch (Exception ignored) {
            }
        }
    }

    //    @Override
//    protected void onPause() {
//        super.onPause();
//        mHandler.removeCallbacks(m_Runnable);
//        finish();
//
//    }
    private void showDialog() {
        View dialogLayout = LayoutInflater.from(DetailActivity.this).inflate(R.layout.dialog_add_review, null);
        addReviewDialog = new Dialog(this);
        addReviewDialog.setContentView(dialogLayout);
        addReviewDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        addReviewDialog.show();

        Button submitReview = addReviewDialog.findViewById(R.id.submitReviewButton);
        submitReview.setOnClickListener(view -> {
            TextInputEditText textInputEditText = addReviewDialog.findViewById(R.id.reviewEditText);
            String reviewText = textInputEditText.getText().toString().trim();
            float numStars = ((RatingBar) addReviewDialog.findViewById(R.id.ratingBar)).getRating();
            Log.d(TAG, "onClick: " + reviewText);
            if (reviewText.isEmpty()) {
                textInputEditText.setError("Review can't be empty");
            } else {
                GeneralUtils.showButtonLoading(submitReview);
                String firstName = sharedPreferences.getString(Constants.SHARED_PREF_FIRST_NAME, "");
                String lastName = sharedPreferences.getString(Constants.SHARED_PREF_LAST_NAME, "");
                String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
                Review review = new Review(numStars, reviewText, firstName + " " + lastName, userId);
                reviewViewModel.submitReview(placeId, review);
                observeOnce(reviewViewModel.mutableLiveDataResponseCode, response -> {
                    if (response == 200) {
                        GeneralUtils.showButtonLoaded(submitReview, null);
                        noReviewsMaterialTextView.setVisibility(View.GONE);
                        firstReviewRatingBar.setRating(numStars);
                        reviewContentTextView.setText(reviewText);
                        visitorNameFirstReviewTextView.setText(firstName + " " + lastName);
                        seeAllButton.setVisibility(View.VISIBLE);

                        m_Runnable.run();
                        firstReviewLinearLayout.setVisibility(View.VISIBLE);

                        addReviewDialog.dismiss();
                    } else {
                        GeneralUtils.showButtonFailed(submitReview, "Failed to submit review", "submit");
                    }
                });

//                addReviewDialog.dismiss();
            }
        });
    }

    public void addReview(View view) {
        showDialog();
    }

    private void chatBot() {
        chatbotFloatingActionButton.setOnClickListener(v -> startActivity(new Intent(DetailActivity.this, ChatbotActivity.class)));
    }

    public void showReviews(View view) {
        Intent intent = new Intent(DetailActivity.this, ReviewActivity.class);
        intent.putExtra("place_id", placeIdToReview);
        startActivity(intent);
    }

    public void backPlace() {
        startActivity(new Intent(DetailActivity.this, Home.class));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

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
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

            mapView.onResume();
        }
    }

    private void getAllItems() {
        detailViewModel.flowable.subscribe(placePagingData -> {

            this.itemPagingAdapter.submitData(getLifecycle(), placePagingData);

        });
//        detailViewModel.itemMutableLiveData.observe(this, placePagingData -> {
//            itemsRecyclerViewAdapter.setItemsArrayList(placePagingData);
//        });

    }

}