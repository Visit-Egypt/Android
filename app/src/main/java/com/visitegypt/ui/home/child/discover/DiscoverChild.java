package com.visitegypt.ui.home.child.discover;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.smarteist.autoimageslider.SliderView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Place;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DiscoverChild extends Fragment {

    private DiscoverChildViewModel mViewModel;
    private View discoverLayOut;
    private SliderView sliderView;
    private SliderAdapter sliderAdapter;
    private LinearLayout shimmerLayout;
    private RecyclerView placesRecyclerView;
    private int[] images;
    private RecyclerView recommendationPlaceRecyclerView;
    private DiscoverPlaceAdapter discoverPlaceAdapter;
    private LinearLayout discoverVerticalLayOut;
    private RecommendationPlaceAdapter recommendationPlaceAdapter;
    private ArrayList<Place> placeArrayList;
    private ArrayList<Place> ourFavouritesArrayList;
    private DiscoverChildViewModel discoverChildViewModel;
    private ShimmerFrameLayout allPlacesShimmer;
    private ShimmerFrameLayout firstRecommendedPlaceShimmer;
    private ShimmerFrameLayout imgPlaceCardshimmer;
    private ShimmerFrameLayout secRecommendedPlaceShimmer;
    private ShimmerFrameLayout sliderShimmerFrameLayout;


    public static DiscoverChild newInstance() {
        return new DiscoverChild();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        discoverLayOut = inflater.inflate(R.layout.discover_child_fragment, container, false);

        initViews();
        createDummyPlaces();
        initViewModels();
        ;
        return discoverLayOut;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DiscoverChildViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initViews() {
        //init images in slider
        images = new int[]{
                R.drawable.splash,
                R.drawable.temple,
                R.drawable.test,
                R.drawable.visitegypt,
                R.drawable.home_search,
        };
        /************************************************/
        //init discoverLayOut
        discoverVerticalLayOut = discoverLayOut.findViewById(R.id.discoverlayout);
        /***********************************************/
        //slider adapter init
        sliderView = discoverLayOut.findViewById(R.id.sliderSliderView);
        sliderAdapter = new SliderAdapter(images);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        /**********************************************/
        //init Places Recycler view
        placeArrayList = new ArrayList<>();
        discoverPlaceAdapter = new DiscoverPlaceAdapter(getContext(), placeArrayList);
        placesRecyclerView = discoverLayOut.findViewById(R.id.placeRecyclerView);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        placesRecyclerView.setAdapter(discoverPlaceAdapter);
        /************************************************/
        // init Recommendation Places Recycler View
        ourFavouritesArrayList = new ArrayList<>();
        recommendationPlaceAdapter = new RecommendationPlaceAdapter(ourFavouritesArrayList, getContext());
        recommendationPlaceRecyclerView = discoverLayOut.findViewById(R.id.recommendationRecyclerView);
        recommendationPlaceRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recommendationPlaceRecyclerView.setAdapter(recommendationPlaceAdapter);
        /**************************************************/
        //init shimmer
        shimmerLayout = discoverLayOut.findViewById(R.id.shimmerLayout);
        allPlacesShimmer = discoverLayOut.findViewById(R.id.allPlacesShimmer);
        sliderShimmerFrameLayout = discoverLayOut.findViewById(R.id.sliderShimmerFrameLayout);
        imgPlaceCardshimmer = discoverLayOut.findViewById(R.id.placesToVisitShimmer);
        firstRecommendedPlaceShimmer = discoverLayOut.findViewById(R.id.recommendationShimmerFrameLayout);
        secRecommendedPlaceShimmer = discoverLayOut.findViewById(R.id.recommendationShimmerFrameLayout2);


    }

    private void initViewModels() {
        discoverChildViewModel = new ViewModelProvider(this).get(DiscoverChildViewModel.class);
        discoverChildViewModel.getAllPlaces();
        discoverChildViewModel.placesMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> placesList) {
                stopShimmerAnimation();
                setLayoutVisible();
                setRecyclerViewsVisible();
                setShimmersGone();
                discoverPlaceAdapter.updatePlacesList(placesList);
            }
        });

    }

    private void setRecyclerViewsVisible() {
        recommendationPlaceRecyclerView.setVisibility(View.VISIBLE);
        placesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void startShimmerAnimation() {
        allPlacesShimmer.startShimmerAnimation();
        sliderShimmerFrameLayout.startShimmerAnimation();
        imgPlaceCardshimmer.startShimmerAnimation();
        firstRecommendedPlaceShimmer.startShimmerAnimation();
        secRecommendedPlaceShimmer.startShimmerAnimation();

    }

    private void stopShimmerAnimation() {
        allPlacesShimmer.stopShimmerAnimation();
        sliderShimmerFrameLayout.stopShimmerAnimation();
        imgPlaceCardshimmer.stopShimmerAnimation();
        firstRecommendedPlaceShimmer.stopShimmerAnimation();
        secRecommendedPlaceShimmer.stopShimmerAnimation();

    }

    private void setLayoutVisible() {
        discoverVerticalLayOut.setVisibility(View.VISIBLE);
        shimmerLayout.setVisibility(View.GONE);


    }

    private void setShimmersGone() {

    }

    @Override
    public void onResume() {
        super.onResume();
        startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopShimmerAnimation();
    }

    private void createDummyPlaces() {
        Place place = new Place();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("https://nileholiday.com/wp-content/uploads/2019/10/All-Temples-Of-Egypt1.jpg");
        place.setImageUrls(arrayList);
        place.setTitle("Karnak Temple");
        place.setLongDescription("The Karnak Temple Complex, commonly known as Karnak, comprises a vast mix of decayed temples, chapels, pylons, and other buildings near Luxor, Egypt.");
        place.setCity("Cairo");
        Place place2 = new Place();
        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add("https://www.egypttoday.com/siteimages/Larg/202106010323272327.jpg");
        place2.setImageUrls(arrayList2);
        place2.setTitle("Masjid Al Hakim");
        place2.setCity("Cairo");
        place2.setLongDescription("The Mosque of al-Hakim, nicknamed al-Anwar, is a major Islamic religious site in Cairo, Egypt.");

        Place place3 = new Place();
        ArrayList<String> arrayList3 = new ArrayList<>();
        arrayList3.add("https://cdn2.civitatis.com/egipto/asuan/excursion-abu-simbel-grid.jpg");
        place3.setImageUrls(arrayList3);
        place3.setTitle("Abu Simbel Temples");
        place3.setLongDescription("Abu Simbel is two massive rock-cut temples in the village of Abu Simbel, Aswan Governorate, Upper Egypt, near the border with Sudan");

        ourFavouritesArrayList.add(place);
        ourFavouritesArrayList.add(place2);
        recommendationPlaceAdapter.updatePlacesList(ourFavouritesArrayList);
    }
}