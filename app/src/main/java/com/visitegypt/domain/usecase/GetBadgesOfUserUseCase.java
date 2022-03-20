package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class GetBadgesOfUserUseCase extends SingleUseCase<List<Badge>> {
    /*
     * Please note that,Khaled
     * this use case returns list of badges with badge task
     */

     UserRepository userRepository;
     SharedPreferences sharedPreferences;
    @Inject
    public GetBadgesOfUserUseCase(@Named("Normal") UserRepository userRepository, SharedPreferences sharedPreferences) {
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    protected Single<List<Badge>> buildSingleUseCase() {
        //final String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID,"");
        String userId = "623705926efafa75d06313e4";
        return userRepository.getUserBadges(userId);
    }
}
