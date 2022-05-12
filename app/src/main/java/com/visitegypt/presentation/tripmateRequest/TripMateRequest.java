package com.visitegypt.presentation.tripmateRequest;

import static com.visitegypt.utils.Constants.CHOSEN_USER_ID;

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

import com.visitegypt.R;
import com.visitegypt.domain.model.User;
import com.visitegypt.presentation.callBacks.OnItemClickCallBack;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.tripmate.TripMate;
import com.visitegypt.presentation.userProfile.UserProfile;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class TripMateRequest extends Fragment implements OnItemClickCallBack {

    private TripMateRequestViewModel tripMateRequestViewModel;
    private View tripMateRequestView;
    private RecyclerView requestsRecyclerView;
    private UserTripMateRequestAdapter userTripMateRequestAdapter;
    private List<User> users = new ArrayList<>();
    private List<com.visitegypt.domain.model.TripMateRequest> tripMateRequests;
    private static final String TAG = "Trip Mate Requests";

    public static TripMateRequest newInstance() {
        return new TripMateRequest();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        tripMateRequestView = inflater.inflate(R.layout.trip_mate_request_fragment, container, false);
        container.removeAllViews();
        initViews();

        return tripMateRequestView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tripMateRequestViewModel = new ViewModelProvider(this).get(TripMateRequestViewModel.class);

       // createFakeData();
        getAllUserRequests();
        mutableLiveDataObserve();

    }

    private void initViews() {
        requestsRecyclerView = tripMateRequestView.findViewById(R.id.requestsRecyclerView);
        userTripMateRequestAdapter = new UserTripMateRequestAdapter(this::onItemCallBack, users);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        requestsRecyclerView.setAdapter(userTripMateRequestAdapter);
        tripMateRequests = new ArrayList<>();


    }

    private void createFakeData() {
        User user1 = new User("Yehia", "Hendy", "https://visitegypt-media-bucket.s3.us-west-2.amazonaws.com/uploads/users/617170dacb2e775f16fc54f2/47b5aa45-f328-4002-85af-bcbb34a28560.jpeg",
                "615df4afdfb3336ce9448939"
        );
        User user3 = new User("Yehia", "Hendy", "https://visitegypt-media-bucket.s3.us-west-2.amazonaws.com/uploads/users/617170dacb2e775f16fc54f2/47b5aa45-f328-4002-85af-bcbb34a28560.jpeg",
                "615df4afdfb3336ce9448939"
        );
        User user2 = new User("Yehia", "Hendy", "https://visitegypt-media-bucket.s3.us-west-2.amazonaws.com/uploads/users/617170dacb2e775f16fc54f2/47b5aa45-f328-4002-85af-bcbb34a28560.jpeg",
                "615df4afdfb3336ce9448939"
        );
        com.visitegypt.domain.model.TripMateRequest tripMateRequest = new com.visitegypt.domain.model.TripMateRequest("Hi", "Helloooooooooo");
        com.visitegypt.domain.model.TripMateRequest tripMateRequest2 = new com.visitegypt.domain.model.TripMateRequest("Hi", "Helloooooooooo");
        com.visitegypt.domain.model.TripMateRequest tripMateRequest3 = new com.visitegypt.domain.model.TripMateRequest("Hi", "Helloooooooooo");
        ArrayList<com.visitegypt.domain.model.TripMateRequest> tripMateRequests = new ArrayList<>();
        tripMateRequests.add(tripMateRequest);
        tripMateRequests.add(tripMateRequest2);
        tripMateRequests.add(tripMateRequest3);
        user1.setTripMateRequests(tripMateRequests);
        user2.setTripMateRequests(tripMateRequests);
        user3.setTripMateRequests(tripMateRequests);
        users.add(user1);
        users.add(user2);
        users.add(user3);
        userTripMateRequestAdapter.updateUserList(users);
    }


    @Override
    public void onItemCallBack(String msg,int flag) {
        if (flag == 0) {
            tripMateRequestViewModel.setRequestId(msg);
            tripMateRequestViewModel.approveTripMateRequest();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(CHOSEN_USER_ID, msg);
            ((Home) getActivity()).changeFragmentWithBundle(new UserProfile(), bundle);
        }
    }

    private void getAllUserRequests() {
//        tripMateRequests = ((Home) getActivity()).getTripMateRequests();
        tripMateRequestViewModel.getTripRequests();
    }

    private void mutableLiveDataObserve() {
        tripMateRequestViewModel.mutableLiveDataUser.observe(getViewLifecycleOwner(),users1 -> {
            Log.d(TAG, "mutableLiveDataObserve: " + users1.get(0).getTripMateRequests());
            userTripMateRequestAdapter.updateUserList(users1);
        });
        tripMateRequestViewModel.mutableLiveDataIsApproved.observe(getViewLifecycleOwner(),aBoolean -> {
            if(aBoolean)
                userTripMateRequestAdapter.removeUser();
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}