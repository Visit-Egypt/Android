package com.visitegypt.domain.usecase;

import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.CompletableUseCase;

import java.util.HashSet;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Completable;

public class UpdateUserInterestUseCase  extends CompletableUseCase<Void> {
    private UserRepository userRepository;
    private HashSet<String> newInterest, removedInterest;

    @Inject
    public UpdateUserInterestUseCase(@Named("Normal") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setNewInterest(HashSet<String> newInterest) {
        this.newInterest = newInterest;
    }

    public void setRemovedInterest(HashSet<String> removedInterest) {
        this.removedInterest = removedInterest;
    }


    @Override
    protected Completable buildCompletableUseCase() {
        return userRepository.updateUserInterests(newInterest, removedInterest);
    }
}
