package com.visitegypt.presentation.tripmate;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.visitegypt.R;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.model.User;
import com.visitegypt.presentation.callBacks.OnFilterUpdate;
import com.visitegypt.utils.Chips;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TripMate extends Fragment implements OnFilterUpdate {

    private TripMateViewModel tripMateViewModel;
    private View tripMateFargment;
    private ChipGroup chipGroup;
    private RecyclerView userRecyclerView;
    private RecyclerView recyclerViewHashtag;
    private UserAdapter userAdapter;
    private FilterChipAdapter hashtagChipAdapter;
    private ArrayList<User> users = new ArrayList<>();
    private List<String> myLabel;
    private HashMap<Integer, String> chipIdMap = new HashMap<>();
    private static final String TAG = "Trip Mate";

    public static TripMate newInstance() {
        return new TripMate();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        tripMateFargment = inflater.inflate(R.layout.trip_mate_fragment, container, false);
        container.removeAllViews();
        initViews();
        Chips.setContext(getContext());
        tripMateViewModel = new ViewModelProvider(this).get(TripMateViewModel.class);
//        createFakeData();
        return tripMateFargment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tripMateViewModel.getUserTags();
        mutableLiveDataObserver();


    }

    private void mutableLiveDataObserver() {
        tripMateViewModel.mutableLiveDataTagNames.observe(getViewLifecycleOwner(), tags -> {
            Chips.createFilterChipsEnhance(tags, chipGroup);
        });
        tripMateViewModel.mutableLiveDataUsers.observe(getViewLifecycleOwner(), users1 -> {

            userAdapter.updateUserList(users1);
        });
    }

    private void initViews() {
        chipGroup = tripMateFargment.findViewById(R.id.chipGroup);
        userRecyclerView = tripMateFargment.findViewById(R.id.userRecyclerView);
        userAdapter = new UserAdapter(getContext(), getParentFragment(), users);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        userRecyclerView.setAdapter(userAdapter);
        Chips.setOnFilterUpdate(this::onFilterUpdate);

//        hashtagChipAdapter = new FilterChipAdapter(myLabel);
//        recyclerViewHashtag = tripMateFargment.findViewById(R.id.recyclerViewHashtag);
//        recyclerViewHashtag.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
//        recyclerViewHashtag.setAdapter(hashtagChipAdapter);


    }


    @Override
    public void onFilterUpdate(List<String> filters) {
        //TODO
        if (filters != null && filters.size() != 0)
            tripMateViewModel.getAllUserTag(filters);
        else
        {
            userAdapter.updateUserList(users);
        }
    }
}