package com.visitegypt.presentation.home.child.activity;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visitegypt.R;
import com.visitegypt.presentation.home.parent.Home;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ActivitiyChild extends Fragment {

    private ActivitiyChildViewModel mViewModel;

    public static ActivitiyChild newInstance() {
        return new ActivitiyChild();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View activityChild = inflater.inflate(R.layout.activitiy_child_fragment, container, false);
        return  activityChild;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ActivitiyChildViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        ((Home)getActivity()).setActionBarTitle("Activity");
        super.onResume();
    }
}