package com.visitegypt.presentation.tripmate;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.chip.ChipGroup;
import com.visitegypt.R;
import com.visitegypt.domain.model.User;
import com.visitegypt.presentation.home.child.discover.DiscoverPlaceAdapter;
import com.visitegypt.utils.Chips;

import java.util.ArrayList;
import java.util.List;

public class TripMate extends Fragment {

    private TripMateViewModel tripMateViewModel;
    private View tripMateFargment;
    private ChipGroup chipGroup;
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> users  = new ArrayList<>();

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

        List<String> myLabel = new ArrayList<>();
        myLabel.add("Travelling");
        myLabel.add("Beach");
        myLabel.add("Reda");
        myLabel.add("Reda");
        myLabel.add("Reda");
        myLabel.add("Reda");
        myLabel.add("Reda");
        myLabel.add("Reda");
        myLabel.add("Reda");
        myLabel.add("Reda");
        for (String name : myLabel){
            chipGroup.addView(Chips.createChipsLabel(name));
        }
        createFakeData();
        return tripMateFargment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
    private void initViews()
    {
        chipGroup = tripMateFargment.findViewById(R.id.chipGroup);
        userRecyclerView = tripMateFargment.findViewById(R.id.userRecyclerView);

        userAdapter = new UserAdapter(getContext(),getParentFragment(), users);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        userRecyclerView.setAdapter(userAdapter);

    }
    private void createFakeData()
    {
        User user1 = new User("Yehia","Hendy","https://visitegypt-media-bucket.s3.us-west-2.amazonaws.com/uploads/users/617170dacb2e775f16fc54f2/47b5aa45-f328-4002-85af-bcbb34a28560.jpeg");
        User user3 = new User("Yehia","Hendy","https://visitegypt-media-bucket.s3.us-west-2.amazonaws.com/uploads/users/617170dacb2e775f16fc54f2/47b5aa45-f328-4002-85af-bcbb34a28560.jpeg");
        User user2 = new User("Yehia","Hendy","https://visitegypt-media-bucket.s3.us-west-2.amazonaws.com/uploads/users/617170dacb2e775f16fc54f2/47b5aa45-f328-4002-85af-bcbb34a28560.jpeg");
        users.add(user1);
        users.add(user2);
        users.add(user3);
        userAdapter.updateUserList(users);
    }


}