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
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

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

    public List<User> getUserTripMates(List<TripMateRequest> tripMateRequests) {
        User user1;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < tripMateRequests.size(); i++) {
            if (!tripMateRequests.get(i).isApproved()) {
                userId = tripMateRequests.get(i).getUserID();
                User user = userRepository.getUser(userId).cache().blockingGet();
                user1 = new User(user.getFirstName(),
                        user.getLastName(),
                        user.getPhotoUrl(),
                        user.getUserId()
                );
                user1.setTripMateSentRequest(tripMateRequests.get(i));
                users.add(user1);

            }
        }
        return users;

    }

    public Single<List<Object>> getUserTripMateEnhance(List<TripMateRequest> tripMateRequests) {
        return Flowable
                .fromIterable(tripMateRequests)
                .parallel()
                .runOn(Schedulers.io())
                .flatMap(new Function<TripMateRequest, Publisher<?>>() {
                    @Override
                    public Publisher<?> apply(TripMateRequest tripMateRequest) throws Throwable {
                        return userRepository.getUser(tripMateRequest.getUserID())
                                .toFlowable()
                                .flatMap(user1 -> {

                                    return Flowable.just(new TripMateSentRequest(tripMateRequest.getId(),
                                            tripMateRequest.getTitle(),
                                            tripMateRequest.getDescription(),
                                            tripMateRequest.getUserID(),
                                            tripMateRequest.isApproved(),
                                            user1.getFirstName() + " " + user.getLastName(),
                                            user1.getPhotoUrl()
                                    ));
                                })
                                ;
                    }
                })
                .sequential()
                .collect(Collectors.toList())

                ;



    }

    @Override
    protected Single<User> buildSingleUseCase() {
        return userRepository
                .getUser(userId);
    }
}
