package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class GetFullBadgeUseCase extends SingleUseCase<List<FullBadge>> {

    private UserRepository userRepository;
    private SharedPreferences sharedPreferences;

    @Inject
    public GetFullBadgeUseCase(@Named("Normal") UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    protected Single<List<FullBadge>> buildSingleUseCase() {
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        return userRepository.getUserFullBadgesDetail(userId);
    }
}
