package com.visitegypt.presentation.home.child.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.imageview.ShapeableImageView;
import com.visitegypt.R;
import com.visitegypt.presentation.home.parent.Home;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ActivitiyChild extends Fragment {

    private ActivitiyChildViewModel mViewModel;
    private ShapeableImageView learnHistory, learnAboutEgypt;

    public static ActivitiyChild newInstance() {
        return new ActivitiyChild();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View activityChild = inflater.inflate(R.layout.activitiy_child_fragment, container, false);
        initViews(activityChild);
        initClickListeners();
        return activityChild;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ActivitiyChildViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        ((Home) getActivity()).setActionBarTitle("Activity");
        super.onResume();
    }


    private void initViews(View v) {
        learnHistory = v.findViewById(R.id.imgPlaceCardNew3);
        learnAboutEgypt = v.findViewById(R.id.imgPlaceCardNew1);
    }

    private void initClickListeners() {
        learnHistory.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {

            }
        });
        learnAboutEgypt.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            } else {

            }
        });
    }
}