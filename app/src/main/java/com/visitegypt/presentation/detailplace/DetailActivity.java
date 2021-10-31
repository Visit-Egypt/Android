package com.visitegypt.presentation.detailplace;

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
            fridayOpeningHours, fVideo, fPhoto, eVideo, ePhoto, children;

    private DetailViewModel detailViewModel;

    private ItemsRecyclerViewAdapter itemsRecyclerViewAdapter;
    private RecyclerView itemsRecyclerView;

    private SliderView sliderView;

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
//      SliderShow
        String url1 = "https://nileholiday.com/wp-content/uploads/2019/10/All-Temples-Of-Egypt1.jpg";
        String url2 = "https://www.egypttoday.com/siteimages/Larg/202106010323272327.jpg";

        ArrayList<Slider> SliderArrayList = new ArrayList<>();

        SliderArrayList.add(new Slider(url1));
        SliderArrayList.add(new Slider(url2));
        SliderAdapter adapter = new SliderAdapter(this, SliderArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }

    private void initViews() {
        fAdult = findViewById(R.id.foreignerAdultPriceTextView);
        fStudent = findViewById(R.id.foreignerStudentPriceTextView);
        eStudent = findViewById(R.id.egyptianStudentTextView);
        eAdult = findViewById(R.id.egyptianAdultPriceTextView);
        fVideo=findViewById(R.id.foreignerVideoPriceTextView);
        fPhoto=findViewById(R.id.foreignerPhotoPriceTextView);
        eVideo=findViewById(R.id.egyptianVideoPriceTextView);
        ePhoto=findViewById(R.id.egyptianPhotoPriceTextView);
        children=findViewById(R.id.childrenPriceTextView);
        desc = findViewById(R.id.descriptionTextView);
        title = findViewById(R.id.titleTextView);

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
                        children.setText(place.getTicketPrices().get(CHILDREN.toString()).toString());

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

            }
        });
    }


}
