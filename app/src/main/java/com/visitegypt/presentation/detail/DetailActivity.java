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

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.smarteist.autoimageslider.SliderView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.Slider;
import com.visitegypt.presentation.chatbot.ChatbotActivity;
import com.visitegypt.presentation.home.HomeRecyclerViewAdapter;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.review.ReviewActivity;
import com.visitegypt.presentation.review.ReviewViewModel;
import com.visitegypt.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "Detail Activity";

    @Inject
    public SharedPreferences sharedPreferences;
    public String placeIdToReview, placeIdFromReview;
    private TextView foreignerAdultPriceTextView, foreignerStudentPriceTextView, egyptianStudentTextView, egyptianAdultPriceTextView, descriptionTextView,
            placeTitleTextView, saturdayOpeningHours, sundayOpeningHours, mondayOpeningHours,
            tuesdayOpeningHours, thursdayOpeningHours, wednesdayOpeningHours,
            fridayOpeningHours, foreignerVideoPriceTextView, foreignerPhotoPriceTextView, egyptianVideoPriceTextView, egyptianPhotoPriceTextView, childrenPriceTextView, locationTextView;
    private MaterialButton addReviewButton;
    private FloatingActionButton chatbotFloatingActionButton;

    private DetailViewModel detailViewModel;
    private ReviewViewModel reviewViewModel;
    private ItemsRecyclerViewAdapter itemsRecyclerViewAdapter;
    private RecyclerView itemsRecyclerView, reviewsRecyclerView;
    private ReviewsRecyclerViewAdapter reviewsRecyclerViewAdapter;
    private SliderView sliderView;
    private SliderAdapter sliderAdapter;
    private ArrayList<Slider> sliderArrayList;
    private Dialog addReviewDialog;
    private String placeId;
    private ShimmerFrameLayout sliderShimmerFrameLayout, titleShimmerFrameLayout, descriptionShimmerFrameLayout, artifactsShimmerFrameLayout, locationShimmerFrameLayout, reviewsShimmerFrameLayout, buttonShimmerFrameLayout, hoursShimmerFrameLayout, pricesShimmerFrameLayout;
    private LinearLayout detailLayout;
    private ScrollView shimmerScrollView;
    private CircularImageView backArrowCircularImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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
        if(placeId == null){
            placeIdToReview = placeIdFromReview;
            placeId=placeIdFromReview;
        }else {
            placeIdToReview = placeId;
        }

        initViews();
        if (placeIdFromReview != null) {
            initViewModel(placeIdFromReview);
        } else {
            initViewModel(placeId);
        }
        chatBot();
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

        saturdayOpeningHours = findViewById(R.id.saturdayOpeningHoursTextView);
        sundayOpeningHours = findViewById(R.id.sundayOpeningHoursTextView);
        mondayOpeningHours = findViewById(R.id.mondayOpeningHoursTextView);
        tuesdayOpeningHours = findViewById(R.id.tuesdayOpeningHoursTextView);
        wednesdayOpeningHours = findViewById(R.id.wednesdayOpeningHoursTextView);
        thursdayOpeningHours = findViewById(R.id.thursdayOpeningHoursTextView);
        fridayOpeningHours = findViewById(R.id.fridayOpeningHoursTextView);

        chatbotFloatingActionButton = findViewById(R.id.fBtnChatbot);
        addReviewButton = findViewById(R.id.writeReviewButton);

        sliderView = findViewById(R.id.sliderSliderView);
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemsRecyclerViewAdapter = new ItemsRecyclerViewAdapter(this);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, true));
        itemsRecyclerView.setAdapter(itemsRecyclerViewAdapter);
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(this);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setAdapter(reviewsRecyclerViewAdapter);

        sliderShimmerFrameLayout = findViewById(R.id.sliderShimmerFrameLayout);
        titleShimmerFrameLayout = findViewById(R.id.titleShimmerFrameLayout);
        descriptionShimmerFrameLayout = findViewById(R.id.descriptionShimmerFrameLayout);
        artifactsShimmerFrameLayout = findViewById(R.id.artifactsShimmerFrameLayout);
        locationShimmerFrameLayout = findViewById(R.id.locationShimmerFrameLayout);
        reviewsShimmerFrameLayout = findViewById(R.id.reviewsShimmerFrameLayout);
        buttonShimmerFrameLayout = findViewById(R.id.buttonShimmerFrameLayout);
        hoursShimmerFrameLayout = findViewById(R.id.hoursShimmerFrameLayout);
        pricesShimmerFrameLayout = findViewById(R.id.pricesShimmerFrameLayout);

        detailLayout = findViewById(R.id.detailActivityLayout);
        shimmerScrollView = findViewById(R.id.shimmerScrollView);
        sliderArrayList = new ArrayList<>();
        sliderAdapter = new SliderAdapter(sliderArrayList);

        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

    }

    private void initViewModel(String placeId) {
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        detailViewModel.getPlace(placeId);
        detailViewModel.getItemsByPlaceId(placeId);

        detailViewModel.itemMutableLiveData.observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                Log.d(TAG, "setting items to recycler view...");
                itemsRecyclerViewAdapter.setItemsArrayList(items);
            }
        });
        backArrowCircularImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPlace();
            }
        });

        detailViewModel.placesMutableLiveData.observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                stopShimmerAnimation();
                setLayoutVisible();
                setShimmersGone();
                if (place.getImageUrls() != null) {
                    for (String url : place.getImageUrls()) {
                        sliderArrayList.add(new Slider(url));
                    }
                    sliderAdapter.updateArrayList(sliderArrayList);
                } else if (place.getDefaultImage() != null) {
                    sliderArrayList.add(new Slider(place.getDefaultImage()));
                } else {
                    Log.e(TAG, "no images found");
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
            }
        });
    }

    private void startShimmerAnimation() {
        sliderShimmerFrameLayout.startShimmerAnimation();
        titleShimmerFrameLayout.startShimmerAnimation();
        descriptionShimmerFrameLayout.startShimmerAnimation();
        artifactsShimmerFrameLayout.startShimmerAnimation();
        locationShimmerFrameLayout.startShimmerAnimation();
        reviewsShimmerFrameLayout.startShimmerAnimation();
        buttonShimmerFrameLayout.startShimmerAnimation();
        hoursShimmerFrameLayout.startShimmerAnimation();
        pricesShimmerFrameLayout.startShimmerAnimation();
    }

    private void stopShimmerAnimation() {
        sliderShimmerFrameLayout.stopShimmerAnimation();
        titleShimmerFrameLayout.stopShimmerAnimation();
        descriptionShimmerFrameLayout.stopShimmerAnimation();
        artifactsShimmerFrameLayout.stopShimmerAnimation();
        locationShimmerFrameLayout.stopShimmerAnimation();
        reviewsShimmerFrameLayout.stopShimmerAnimation();
        buttonShimmerFrameLayout.stopShimmerAnimation();
        hoursShimmerFrameLayout.stopShimmerAnimation();
        pricesShimmerFrameLayout.stopShimmerAnimation();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopShimmerAnimation();
    }

    private void showDialog() {
        View dialogLayout = LayoutInflater.from(DetailActivity.this).inflate(R.layout.dialog_add_review, null);
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

    private void chatBot() {
        chatbotFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, ChatbotActivity.class));
            }
        });
    }

    public void showReviews(View view) {
        Intent intent = new Intent(DetailActivity.this, ReviewActivity.class);
        intent.putExtra("place_id", placeIdToReview);
        startActivity(intent);
    }

    public void backPlace() {
        startActivity(new Intent(DetailActivity.this, Home.class));
    }
}