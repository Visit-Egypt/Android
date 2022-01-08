package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;

import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UploadUserPhotoUseCase extends SingleUseCase<UploadResponse> {
    private SharedPreferences sharedPreferences;
    private UserRepository userRepository;
    private String contentType;
    @Inject
    public UploadUserPhotoUseCase (UserRepository userRepository, SharedPreferences sharedPreferences){
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    @Override
    protected Single<UploadResponse> buildSingleUseCase() {
        final String userId = this.sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        return userRepository.uploadUserPhoto(userId, contentType);
    }
}
