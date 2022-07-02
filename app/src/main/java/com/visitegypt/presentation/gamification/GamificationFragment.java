package com.visitegypt.presentation.gamification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.visitegypt.R;
import com.visitegypt.presentation.place.PlacesActivity;

import javax.inject.Inject;

public class GamificationFragment extends Fragment {
    private static final String TAG = "Gamification fragment";
    @Inject
    public SharedPreferences sharedPreferences;
    private ConstraintLayout luxorCityLayout, alexCityLayout, alsharkiaCityLayout, cairoCityLayout;

    private View gamificationLayout;

    public static GamificationFragment newInstance() {
        return new GamificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        gamificationLayout = inflater.inflate(R.layout.gamification_fragment, container, false);
        initViews();
        init();
        return gamificationLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    private void initViews() {
        luxorCityLayout = gamificationLayout.findViewById(R.id.luxorCityLayout);
        alexCityLayout = gamificationLayout.findViewById(R.id.alexCityLayout);
        alsharkiaCityLayout = gamificationLayout.findViewById(R.id.alsharkiaCityLayout);
        cairoCityLayout = gamificationLayout.findViewById(R.id.cairoCityLayout);

    }

    private void init() {
        luxorCityLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PlacesActivity.class);
            intent.putExtra("city_name", "Luxor");
            startActivity(intent);

        });
        cairoCityLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PlacesActivity.class);
            intent.putExtra("city_name", "Cairo");
            startActivity(intent);

        });
        alsharkiaCityLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PlacesActivity.class);
            intent.putExtra("city_name", "Al-Sharkia");
            startActivity(intent);

        });
        alexCityLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PlacesActivity.class);
            intent.putExtra("city_name", "Alexandria");
            startActivity(intent);

        });

    }

}