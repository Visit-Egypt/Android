package com.visitegypt.ui.subscription;

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

import com.visitegypt.databinding.FragmentSubscriptionBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SubscriptionFragment extends Fragment {

    private SubscriptionViewModel subscriptionViewModel;
    private FragmentSubscriptionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subscriptionViewModel =
                new ViewModelProvider(this).get(SubscriptionViewModel.class);

        binding = FragmentSubscriptionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        subscriptionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}