package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;
import android.util.Log;

import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.domain.model.TripMateSentRequest;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GetUserUseCase extends SingleUseCase<User> {
    private static final String TAG = "get user use case";
    private User user = new User();
    private String userId;
    private String email;
    private UserRepository userRepository;
    private SharedPreferences sharedPreferences;


    @Inject
    public GetUserUseCase(@Named("Normal") UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    public void setUser(String userId, String email) {
        Log.d(TAG, "setUser: " + email);
        user.setUserId(userId);
        user.setEmail(email);
        this.userId = userId;
        this.email = email;
    }

    public void saveUserData(User user) {
        sharedPreferences.edit()
                .putString(Constants.SHARED_PREF_FIRST_NAME, user.getFirstName() + " " + user.getLastName())
                .putString(Constants.SHARED_PREF_PHONE_NUMBER, user.getPhoneNumber())
                .putString(Constants.SHARED_PREF_EMAIL, email)
                .apply();
    }


    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository
                .getUser(userId);
    }

    public Single<List<String>> getUserInterest() {
        return userRepository
                .getUser(userId)
                .subscribeOn(Schedulers.io())
                .map(user1 -> user1.getInterests())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }
}
