package com.visitegypt.presentation.detailplace;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.smarteist.autoimageslider.SliderView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Slider;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    TextView fAdult, fStudent, eStudent, eAdult,desc,title;

    DetailViewModel detailViewModel;
    private static final String TAG = "Detail Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.getPlace();
        detailViewModel.mutableLiveData.observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
            }
        });


        fAdult = findViewById(R.id.fAdult);
        fStudent = findViewById(R.id.fStudent);
        eStudent = findViewById(R.id.eStudent);
        eAdult = findViewById(R.id.eAdult);
        desc=findViewById(R.id.description);
        title=findViewById(R.id.name);

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

        fAdult.setText(fAdult.getText() + ticketPrices.get("Foreigner Adult").toString());
        fStudent.setText(fStudent.getText() + ticketPrices.get("Foreigner Student").toString());
        eStudent.setText(eStudent.getText() + ticketPrices.get("Egyptian Student").toString());
        eAdult.setText(eAdult.getText() + ticketPrices.get("Egyptian Adult").toString());

        title.setText("Karnak Temple");
        desc.setText("The Karnak Temple Complex, commonly known as Karnak, comprises a vast mix of decayed temples, chapels, pylons, and other buildings near Luxor, Egypt.");

    }


}
