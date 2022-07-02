package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.FullExplore;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class GetUserExploresUseCase extends SingleUseCase<List<FullExplore>> {

    private UserRepository userRepository;
    private SharedPreferences sharedPreferences;

    @Inject
    public GetUserExploresUseCase(@Named("Normal") UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    protected Single<List<FullExplore>> buildSingleUseCase() {
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        return userRepository.getUserFullExploreDetail(userId);
    }
}
