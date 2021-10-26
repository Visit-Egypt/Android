package com.visitegypt.presentation.detailplace;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.smarteist.autoimageslider.SliderView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Slider;
import com.visitegypt.presentation.home.HomeRecyclerViewAdapter;
import com.visitegypt.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "Detail Activity";

    TextView fAdult, fStudent, eStudent, eAdult, desc, title;

    private DetailViewModel detailViewModel;

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


        HashMap<String, Integer> ticketPrices = new HashMap<String, Integer>();
//        Map<String, Integer> ticketPrices = new Map<String, Integer>();
//        ticketPrices.put("Children Under 6 Years",0);
//        ticketPrices.put("Egyptian Personal Video",10);
//        ticketPrices.put("Egyptian Personal Photography",20);
        ticketPrices.put("Egyptian Student", 30);
        ticketPrices.put("Egyptian Adult", 40);
//        ticketPrices.put("Foreigner Adult Personal Video",100);
//        ticketPrices.put("Foreigner Adult Personal Photography",200);
        ticketPrices.put("Foreigner Student", 300);
        ticketPrices.put("Foreigner Adult", 400);
        //      SliderShow
        String url1 = "https://nileholiday.com/wp-content/uploads/2019/10/All-Temples-Of-Egypt1.jpg";
        String url2 = "https://www.egypttoday.com/siteimages/Larg/202106010323272327.jpg";

        ArrayList<Slider> SliderArrayList = new ArrayList<>();
        SliderView sliderView = findViewById(R.id.slider);
        SliderArrayList.add(new Slider(url1));
        SliderArrayList.add(new Slider(url2));
        SliderAdapter adapter = new SliderAdapter(this, SliderArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        fAdult.setText(ticketPrices.get("Foreigner Adult").toString() + " EGP");
        fStudent.setText(ticketPrices.get("Foreigner Student").toString() + " EGP");
        eStudent.setText(ticketPrices.get("Egyptian Student").toString() + " EGP");
        eAdult.setText(ticketPrices.get("Egyptian Adult").toString() + " EGP");

        title.setText("Karnak Temple");
        desc.setText("The Karnak Temple Complex, commonly known as Karnak, comprises a vast mix of decayed temples, chapels, pylons, and other buildings near Luxor, Egypt.");

    }

    private void initViews() {
        fAdult = findViewById(R.id.foreignerAdultPriceTextView);
        fStudent = findViewById(R.id.foreignerStudentPriceTextView);
        eStudent = findViewById(R.id.egyptianStudentTextView);
        eAdult = findViewById(R.id.egptianAdultPriceTextView);
        desc = findViewById(R.id.descriptionTextView);
        title = findViewById(R.id.name);
    }

    private void initViewModel(String placeId) {
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.getPlace(placeId);
        detailViewModel.mutableLiveData.observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                Log.d(TAG, "onChanged: " + place.getTitle());
                if (place.getTicketPrices() != null) {
                    try {
                        fAdult.setText(place.getTicketPrices().get(Constants.CustomerType.FOREIGNER_ADULT));
                        eAdult.setText(place.getTicketPrices().get(Constants.CustomerType.EGYPTIAN_ADULT));
                        eStudent.setText(place.getTicketPrices().get(Constants.CustomerType.FOREIGNER_STUDENT));
                        fStudent.setText(place.getTicketPrices().get(Constants.CustomerType.FOREIGNER_STUDENT));
                    } catch (Exception ignored) {
                        
                    }
                }
                title.setText(place.getTitle());
                desc.setText(place.getDescription());

            }
        });
    }


}
