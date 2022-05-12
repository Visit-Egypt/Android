package com.visitegypt.presentation.tripmateRequest;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.ApproveTripMateUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TripMateRequestViewModel extends ViewModel {
    private String requestId;
    private GetUserUseCase getUserUseCase;
    private ApproveTripMateUseCase approveTripMateUseCase;
    private static final String TAG = "Trip Mate Request View Model";
    private String userId;
    MutableLiveData<List<User>> mutableLiveDataUser = new MutableLiveData<>();
    MutableLiveData<Boolean> mutableLiveDataIsApproved = new MutableLiveData<>();
    List<TripMateRequest> tripMateRequests = new ArrayList<>();
    SharedPreferences sharedPreferences;
    @Inject
    public TripMateRequestViewModel(GetUserUseCase getUserUseCase, ApproveTripMateUseCase approveTripMateUseCase,SharedPreferences sharedPreferences) {
        this.getUserUseCase = getUserUseCase;
        this.approveTripMateUseCase = approveTripMateUseCase;
        this.sharedPreferences = sharedPreferences;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void getTripMateUsers() {
        List<User> users = getUserUseCase.getUserTripMates(tripMateRequests);
        if (users.size() != 0)
            mutableLiveDataUser.setValue(users);
    }
    public void getTripRequests()
    {
        userId = sharedPreferences.getString(SHARED_PREF_USER_ID,"");
        getUserUseCase.setUser(userId,"");
        getUserUseCase.execute(user -> {
            tripMateRequests = user.getTripMateRequests();
            getTripMateUsers();
        },throwable -> {
            Log.d(TAG, "getTripRequests: ");

        });
    }

    public void approveTripMateRequest() {
        approveTripMateUseCase.setRequestId(requestId);
        approveTripMateUseCase.execute(user -> {
            int size = user.getTripMateRequests().size();

            for (int i = 0; i < size; i++) {
                if ((user.getTripMateRequests().get(i).getId().equals(requestId)) && user.getTripMateRequests().get(i).isApproved())
                    mutableLiveDataIsApproved.setValue(true);

            }


        }, throwable -> {
            Log.d(TAG, "approveTripMateRequest: " + throwable.getMessage());
        });
    }
}