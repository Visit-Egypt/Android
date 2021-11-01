package com.visitegypt.presentation.detailplace;

import static android.view.View.GONE;
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

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smarteist.autoimageslider.SliderView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Slider;
import com.visitegypt.presentation.home.HomeRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "Detail Activity";

    private TextView fAdult, fStudent, eStudent, eAdult, desc,
            title, saturdayOpeningHours, sundayOpeningHours, mondayOpeningHours,
            tuesdayOpeningHours, thursdayOpeningHours, wednesdayOpeningHours,
            fridayOpeningHours, fVideo, fPhoto, eVideo, ePhoto, children, location, noReviews;

    private LinearLayout locationLayout;

    private DetailViewModel detailViewModel;

    private ItemsRecyclerViewAdapter itemsRecyclerViewAdapter;
    private RecyclerView itemsRecyclerView, reviewsRecyclerView;

    private ReviewsRecyclerViewAdapter reviewsRecyclerViewAdapter;
    private SliderView sliderView;

    private SliderAdapter sliderAdapter;

    private ArrayList<Slider> sliderArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String placeId;
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
        Log.d(TAG, "Place ID: " + placeId);
        initViews();
        initViewModel(placeId);
    }

    private void initViews() {
        fAdult = findViewById(R.id.foreignerAdultPriceTextView);
        fStudent = findViewById(R.id.foreignerStudentPriceTextView);
        eStudent = findViewById(R.id.egyptianStudentTextView);
        eAdult = findViewById(R.id.egyptianAdultPriceTextView);
        fVideo = findViewById(R.id.foreignerVideoPriceTextView);
        fPhoto = findViewById(R.id.foreignerPhotoPriceTextView);
        eVideo = findViewById(R.id.egyptianVideoPriceTextView);
        ePhoto = findViewById(R.id.egyptianPhotoPriceTextView);
        children = findViewById(R.id.childrenPriceTextView);
        desc = findViewById(R.id.descriptionTextView);
        title = findViewById(R.id.titleTextView);
        location = findViewById(R.id.locationTextView);
        noReviews = findViewById(R.id.noReviewsTextView);

        saturdayOpeningHours = findViewById(R.id.saturdayOpeningHoursTextView);
        sundayOpeningHours = findViewById(R.id.sundayOpeningHoursTextView);
        mondayOpeningHours = findViewById(R.id.mondayOpeningHoursTextView);
        tuesdayOpeningHours = findViewById(R.id.tuesdayOpeningHoursTextView);
        wednesdayOpeningHours = findViewById(R.id.wednesdayOpeningHoursTextView);
        thursdayOpeningHours = findViewById(R.id.thursdayOpeningHoursTextView);
        fridayOpeningHours = findViewById(R.id.fridayOpeningHoursTextView);

        sliderView = findViewById(R.id.sliderSliderView);

        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemsRecyclerViewAdapter = new ItemsRecyclerViewAdapter(this);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsRecyclerView.setAdapter(itemsRecyclerViewAdapter);

        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(this);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setAdapter(reviewsRecyclerViewAdapter);

        locationLayout = findViewById(R.id.locationLayout);

        sliderArrayList = new ArrayList<>();
        sliderAdapter = new SliderAdapter(sliderArrayList);

        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }

    private void initViewModel(String placeId) {
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.getPlace(placeId);
        detailViewModel.getItemsByPlaceId(placeId);

        detailViewModel.itemMutableLiveData.observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                Log.d(TAG, "setting items to recycler view...");
                itemsRecyclerViewAdapter.setItemsArrayList(items);
                //itemsRecyclerViewAdapter.setItemsArrayList(items);
            }
        });

        detailViewModel.placesMutableLiveData.observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                Log.d(TAG, "onChanged: " + place.getTitle());

                if (place.getImageUrls() != null) {
                    Log.d(TAG, "images found: " + place.getImageUrls().toString());
                    for (String url : place.getImageUrls()) {
                        sliderArrayList.add(new Slider(url));
                    }
                    sliderAdapter.updateArrayList(sliderArrayList);
                } else {
                    Log.e(TAG, "no images found");
                }
                if (place.getTicketPrices() != null) {
                    try {
                        fAdult.setText(place.getTicketPrices().get(FOREIGNER_ADULT.toString()).toString());
                        eAdult.setText(place.getTicketPrices().get(EGYPTIAN_ADULT.toString()).toString());
                        eStudent.setText(place.getTicketPrices().get(EGYPTIAN_STUDENT.toString()).toString());
                        fStudent.setText(place.getTicketPrices().get(FOREIGNER_STUDENT.toString()).toString());
                        fPhoto.setText(place.getTicketPrices().get(FOREIGNER_ADULT_PHOTO.toString()).toString());
                        fVideo.setText(place.getTicketPrices().get(FOREIGNER_ADULT_VIDEO.toString()).toString());
                        eVideo.setText(place.getTicketPrices().get(EGYPTIAN_VIDEO.toString()).toString());
                        ePhoto.setText(place.getTicketPrices().get(EGYPTIAN_PHOTO.toString()).toString());
                        if (place.getTicketPrices().get(CHILDREN.toString()) == 0) {
                            children.setText("FREE");
                        } else {
                            children.setText(place.getTicketPrices().get(CHILDREN.toString()).toString());
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
                title.setText(place.getTitle());
                desc.setText(place.getLongDescription());
                if (place.getReviews() != null & !place.getReviews().isEmpty()) {
                    noReviews.setVisibility(GONE);
                    reviewsRecyclerViewAdapter.setReviewsArrayList(place.getReviews());
                    Log.d(TAG, "reviews available");
                } else {
                    Log.d(TAG, "no reviews available ");
                }
                if (place.getLocationDescription() != null) {
                    Log.d(TAG, "location provided: " + place.getLocationDescription());
                    location.setText(place.getLocationDescription());
                } else {
                    locationLayout.setVisibility(GONE);
                }
            }
        });
    }


}
