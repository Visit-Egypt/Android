package com.visitegypt.presentation.home.child.discover;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.smarteist.autoimageslider.SliderView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.presentation.home.child.discover.allPlaces.DiscoverChildAllPlacesActivity;
import com.visitegypt.presentation.home.child.discover.peopleMayLike.PeopleMayLike;
import com.visitegypt.presentation.home.child.discover.youMayLike.YouMayLike;
import com.visitegypt.presentation.home.parent.Home;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DiscoverChild extends Fragment {
    private static final String TAG = "Discover Child Fragment";

    private DiscoverChildViewModel mViewModel;
    private View discoverLayOut;
    private SliderView sliderView;
    private SliderAdapter sliderAdapter;
    private LinearLayout shimmerLayout;
    private RecyclerView discoverPlacesRecyclerView;
    private int[] images;
    private RecyclerView youMayLikeRecyclerView, peopleMayLikeRecyclerView;
    private DiscoverPlaceAdapter discoverPlaceAdapter;
    private LinearLayout discoverVerticalLayOut;
    private RecommendationPlaceAdapter placesYouMayLikeAdapter,placesPeopleMayLikeAdapter;
    private List<Place> placesYouMayLike,placesPeopleMayLike, discoverPlacesArrayList;
    private DiscoverChildViewModel discoverChildViewModel;
    private ShimmerFrameLayout allPlacesShimmer,
            firstRecommendedPlaceShimmer,
            secRecommendedPlaceShimmer,
            sliderShimmerFrameLayout;
    private MaterialButton btnAllPlaces,btnYouMayLike,btnOtherPeopleMayLike;


    public static DiscoverChild newInstance() {
        return new DiscoverChild();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        discoverLayOut = inflater.inflate(R.layout.discover_child_fragment, container, false);
        initViews();
//        createDummyPlaces();
        onClickListeners();
        return discoverLayOut;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DiscoverChildViewModel.class);
        // TODO: Use the ViewModel
        initViewModels();
        getAllPlaces();
        getRecommendedPlaces();
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
        /**************************************************/
        btnAllPlaces = discoverLayOut.findViewById(R.id.allPlacesButton);
        /************************************************/
        //init discoverLayOut
        discoverVerticalLayOut = discoverLayOut.findViewById(R.id.discoverLinearLayout);
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
        btnYouMayLike = discoverLayOut.findViewById(R.id.btnYouMayLike);
        discoverPlacesRecyclerView = discoverLayOut.findViewById(R.id.placeRecyclerView);
        discoverPlacesArrayList = new ArrayList<>();
        discoverPlacesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        discoverPlaceAdapter = new DiscoverPlaceAdapter(getContext(), discoverPlacesArrayList);
        discoverPlacesRecyclerView.setAdapter(discoverPlaceAdapter);


        /************************************************/
        // init Recommendation Places Recycler View
        btnOtherPeopleMayLike = discoverLayOut.findViewById(R.id.btnOtherPeopleMayLike);
        placesYouMayLike = new ArrayList<>();
        placesYouMayLikeAdapter = new RecommendationPlaceAdapter(placesYouMayLike, getContext(),0);
        youMayLikeRecyclerView = discoverLayOut.findViewById(R.id.youMayLikeRecyclerView);
        youMayLikeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        youMayLikeRecyclerView.setAdapter(placesYouMayLikeAdapter);
        /************************************************/
        // init Recommendation Places Recycler View
        placesPeopleMayLike = new ArrayList<>();
        placesPeopleMayLikeAdapter = new RecommendationPlaceAdapter(placesPeopleMayLike, getContext(),0);
        peopleMayLikeRecyclerView = discoverLayOut.findViewById(R.id.peopleMayLikeRecyclerView);
        peopleMayLikeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        peopleMayLikeRecyclerView.setAdapter(placesPeopleMayLikeAdapter);
        /**************************************************/
        //init shimmer
        shimmerLayout = discoverLayOut.findViewById(R.id.shimmerLayout);
        allPlacesShimmer = discoverLayOut.findViewById(R.id.allPlacesShimmer);
        sliderShimmerFrameLayout = discoverLayOut.findViewById(R.id.sliderShimmerFrameLayout);
        firstRecommendedPlaceShimmer = discoverLayOut.findViewById(R.id.recommendationShimmerFrameLayout);
        secRecommendedPlaceShimmer = discoverLayOut.findViewById(R.id.recommendationShimmerFrameLayout2);


    }

    private void initViewModels() {
        discoverChildViewModel = new ViewModelProvider(this).get(DiscoverChildViewModel.class);
        discoverChildViewModel.getCachedPlaces();


    }

    private void setRecyclerViewsVisible() {
        youMayLikeRecyclerView.setVisibility(View.VISIBLE);
        discoverPlacesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void startShimmerAnimation() {
        allPlacesShimmer.startShimmerAnimation();
        sliderShimmerFrameLayout.startShimmerAnimation();
        firstRecommendedPlaceShimmer.startShimmerAnimation();
        secRecommendedPlaceShimmer.startShimmerAnimation();

    }

    private void stopShimmerAnimation() {
        allPlacesShimmer.stopShimmerAnimation();
        sliderShimmerFrameLayout.stopShimmerAnimation();
        firstRecommendedPlaceShimmer.stopShimmerAnimation();
        secRecommendedPlaceShimmer.stopShimmerAnimation();
        shimmerLayout.setVisibility(View.GONE);
    }

    private void setLayoutVisible() {
        discoverVerticalLayOut.setVisibility(View.VISIBLE);
        shimmerLayout.setVisibility(View.GONE);
    }

    private void getAllPlaces() {
        discoverChildViewModel.placesMutableLiveData.observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
                discoverPlacesArrayList.addAll(place);
                setRecyclerViewsVisible();
                stopShimmerAnimation();
                setLayoutVisible();
                discoverPlaceAdapter.updatePlacesList(discoverPlacesArrayList);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Home) getActivity()).setActionBarTitle("Discover");
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

        placesYouMayLike.add(place);
        placesYouMayLike.add(place2);
        placesYouMayLikeAdapter.updatePlacesList(placesYouMayLike);
    }

    private void onClickListeners() {
        btnAllPlaces.setOnClickListener(view -> {
//            ((Home) getActivity()).changeFragment(new DiscoverChildAllPlaces());
            Intent intent = new Intent(getContext(), DiscoverChildAllPlacesActivity.class);
            startActivity(intent);
        });
        btnYouMayLike.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), YouMayLike.class);
            startActivity(intent);
        });
        btnOtherPeopleMayLike.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), PeopleMayLike.class);
            startActivity(intent);
        });
    }
    private void getRecommendedPlaces()
    {
        discoverChildViewModel.getRecommendedPlaces();
        discoverChildViewModel.recommendationPlacesMutableLiveData.observe(getViewLifecycleOwner(), place -> {
            if (place != null) {
                placesYouMayLike.addAll(place.getUserLikePlaces());
                placesYouMayLikeAdapter.updatePlacesList(placesYouMayLike);
                btnYouMayLike.setEnabled(true);
                placesPeopleMayLike.addAll(place.getPeopleLikePlaces());
                placesPeopleMayLikeAdapter.updatePlacesList(placesPeopleMayLike);
                btnOtherPeopleMayLike.setEnabled(true);
            }
        });
    }





}