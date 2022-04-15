package com.visitegypt.presentation.item;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.smarteist.autoimageslider.SliderView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Slider;
import com.visitegypt.presentation.detail.DetailActivity;
import com.visitegypt.presentation.detail.SliderAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ItemActivity extends AppCompatActivity {
    private static final String TAG = "Item Activity";

    @Inject
    public SharedPreferences sharedPreferences;
    public ArrayList images;
    public String title, description, placeId;
    private MaterialTextView itemDescriptionTextView, itemTitleTextView;
    private ItemViewModel itemViewModel;
    private SliderView sliderView;
    private SliderAdapter sliderAdapter;
    private ArrayList<Slider> sliderArrayList;
    private CircularImageView backArrowImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        title = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        images = getIntent().getStringArrayListExtra("images");
        placeId = getIntent().getStringExtra("place_id");

        initViews();
        initViewModel(title, description, images, placeId);
    }

    private void initViews() {
        itemDescriptionTextView = findViewById(R.id.itemDescriptionTextView);
        itemTitleTextView = findViewById(R.id.itemTitleTextView);
        backArrowImageButton = findViewById(R.id.backArrowCircularImageButton);
        sliderView = findViewById(R.id.sliderSliderView);
        sliderArrayList = new ArrayList<>();
        sliderAdapter = new SliderAdapter(sliderArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }

    private void initViewModel(String title, String description, ArrayList images, String placeId) {
        itemTitleTextView.setText(title);
        itemDescriptionTextView.setText(description);
        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                sliderArrayList.add(new Slider(images.get(i).toString()));
            }
            sliderAdapter.updateArrayList(sliderArrayList);
        }
        backArrowImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemActivity.this, DetailActivity.class);

                intent.putExtra("place_id", placeId);
                startActivity(intent);
            }
        });
    }
}