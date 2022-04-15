package com.visitegypt.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.visitegypt.databinding.FragmentActivitiesBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ActivitiesFragment extends Fragment {

    private ActivitiesViewModel activitiesViewModel;
    private FragmentActivitiesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        activitiesViewModel =
                new ViewModelProvider(this).get(ActivitiesViewModel.class);

        binding = FragmentActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}