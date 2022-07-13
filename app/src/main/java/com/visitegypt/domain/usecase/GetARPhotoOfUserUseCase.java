package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.ARResponsePage;
import com.visitegypt.domain.repository.ARRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;

public class GetARPhotoOfUserUseCase extends SingleUseCase<ARResponsePage> {
    ARRepository arRepository;
    SharedPreferences sharedPreferences;

    @Inject
    public GetARPhotoOfUserUseCase( ARRepository arRepository, SharedPreferences sharedPreferences) {
        this.arRepository = arRepository;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    protected Single<ARResponsePage> buildSingleUseCase() {
        return arRepository.getARResponse(sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, null));
    }
}
