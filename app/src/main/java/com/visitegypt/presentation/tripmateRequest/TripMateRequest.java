package com.visitegypt.presentation.tripmateRequest;

import static com.visitegypt.utils.Constants.CHOSEN_USER_ID;

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
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.visitegypt.R;
import com.visitegypt.domain.model.TripMateSentRequest;
import com.visitegypt.domain.model.User;
import com.visitegypt.presentation.callBacks.OnItemClickCallBack;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.userProfile.UserProfile;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TripMateRequest extends Fragment implements OnItemClickCallBack {

    private TripMateRequestViewModel tripMateRequestViewModel;
    private View tripMateRequestView;
    private RecyclerView requestsRecyclerView;
    private TripMateRequestAdapter userTripMateRequestAdapter;
    private List<TripMateSentRequest> tripMateSentRequests = new ArrayList<>();
    private ShimmerFrameLayout firstReq, secReq, thirdReq, fourthReq;
    private LinearLayout linearLayout;
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
        getAllUserRequests();
        mutableLiveDataObserve();

    }

    private void initViews() {
        requestsRecyclerView = tripMateRequestView.findViewById(R.id.requestsRecyclerView);
        userTripMateRequestAdapter = new TripMateRequestAdapter(this::onItemCallBack, tripMateSentRequests);
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        requestsRecyclerView.setAdapter(userTripMateRequestAdapter);
        tripMateRequests = new ArrayList<>();
        firstReq = tripMateRequestView.findViewById(R.id.firstReq);
        secReq = tripMateRequestView.findViewById(R.id.secReq);
        thirdReq = tripMateRequestView.findViewById(R.id.thirdReq);
        fourthReq = tripMateRequestView.findViewById(R.id.fourthReq);
        linearLayout = tripMateRequestView.findViewById(R.id.userProfileShimmer);


    }

    @Override
    public void onItemCallBack(String msg, int flag) {
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
        tripMateRequestViewModel.getTripRequests();

    }

    private void mutableLiveDataObserve() {
        tripMateRequestViewModel.mutableLiveDataUser.observe(getViewLifecycleOwner(), tripMateSentRequests1 -> {
            stopShimmerAnimation();
            userTripMateRequestAdapter.updateTripMateSentRequestList(tripMateSentRequests1);
        });
        tripMateRequestViewModel.mutableLiveDataIsApproved.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean)
                userTripMateRequestAdapter.removeUser();
        });

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

    @Override
    public void onStop() {
        super.onStop();
        stopShimmerAnimation();
    }

    private void startShimmerAnimation() {
        firstReq.startShimmerAnimation();
        secReq.startShimmerAnimation();
        thirdReq.startShimmerAnimation();
        fourthReq.startShimmerAnimation();
        requestsRecyclerView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
    }
    private void stopShimmerAnimation() {
        firstReq.stopShimmerAnimation();
        secReq.stopShimmerAnimation();
        thirdReq.stopShimmerAnimation();
        fourthReq.stopShimmerAnimation();
        requestsRecyclerView.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
    }

}