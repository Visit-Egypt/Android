package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.domain.model.TripMateSentRequest;
import com.visitegypt.domain.repository.UserRepository;

import org.reactivestreams.Publisher;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GetUserTripMates {
    private UserRepository userRepository;

    @Inject
    public GetUserTripMates(@Named("Normal") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public @NonNull Single<List<TripMateSentRequest>> getUserTripMate(List<TripMateRequest> tripMateRequests) {
        return Flowable
                .fromIterable(tripMateRequests)
                .parallel()
                .runOn(Schedulers.io())
                .filter(tripMateRequest -> !tripMateRequest.isApproved())
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
                                            user1.getFirstName() + " " + user1.getLastName(),
                                            user1.getPhotoUrl(),
                                            user1.getFollowers().size()
                                    ));
                                })
                                ;
                    }
                })
                .sequential()
                .map(o -> new TripMateSentRequest(
                        ((TripMateSentRequest) o).getId(),
                        ((TripMateSentRequest) o).getTitle(),
                        ((TripMateSentRequest) o).getDescription(),
                        ((TripMateSentRequest) o).getUserID(),
                        ((TripMateSentRequest) o).isApproved(),
                        ((TripMateSentRequest) o).getUserName(),
                        ((TripMateSentRequest) o).getPhotoUrl(),
                        ((TripMateSentRequest) o).getFollowersNumber()
                ))
                .toList()
                ;


    }
}
