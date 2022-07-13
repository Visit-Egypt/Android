package com.visitegypt.presentation.home.child.activity.ARCharacter;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.GetARPhotoOfUserUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.UploadUserPhotoUseCase;
import com.visitegypt.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CharacterViewModel extends ViewModel implements UploadUserPhotoUseCase.uploadPhotoApiCallBack {
    private static final String TAG = "Character ViewModel";

    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> isImageUploaded = new MutableLiveData<>();
    MutableLiveData<String> arPNGFilesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> arOBJFilesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> arMTLFilesMutableLiveData = new MutableLiveData<>();
    GetARPhotoOfUserUseCase getARPhotoOfUserUseCase;
    private UploadUserPhotoUseCase uploadUserPhotoUseCase;
    private GetUserUseCase getUserUseCase;
    private SharedPreferences sharedPreferences;
    private List<String> imageList = new ArrayList<>();

    @Inject
    public CharacterViewModel(SharedPreferences sharedPreferences,
                              UploadUserPhotoUseCase uploadUserPhotoUseCase,
                              GetUserUseCase getUserUseCase, GetARPhotoOfUserUseCase getARPhotoOfUserUseCase) {
        this.sharedPreferences = sharedPreferences;
        this.uploadUserPhotoUseCase = uploadUserPhotoUseCase;
        this.getUserUseCase = getUserUseCase;
        this.getARPhotoOfUserUseCase = getARPhotoOfUserUseCase;
    }

    public void uploadUserProfilePhoto(File userPhoto, String contentType) {
        Log.d(TAG, "uploadUserProfilePhoto: wwwwwwww");
        uploadUserPhotoUseCase.setContentType(contentType);
        Log.d(TAG, "uploadUserProfilePhoto: aaaaaaaa");
        uploadUserPhotoUseCase.setUserFile(userPhoto);
        Log.d(TAG, "uploadUserProfilePhoto: aaaaaaaa" + contentType + " " + userPhoto);
        uploadUserPhotoUseCase.uploadARPhoto();
        Log.d(TAG, "uploadUserProfilePhoto: aaaaaa");
    }

    public void initCallBack() {
        Log.d(TAG, "initCallBack: a");
        uploadUserPhotoUseCase.setUploadPhotoApiCallBack(this::confirmUpload);
        Log.d(TAG, "initCallBack: zz");
    }

    @Override
    public void confirmUpload(int code, List<String> url) {
        Log.d(TAG, "confirmUpload: done upload");
        if (code == 200) {
            isImageUploaded.setValue(true);
            imageList = url;
        }
    }

    public void getUser() {
        Log.d(TAG, "getUser: dd");
        String userId = sharedPreferences.getString(SHARED_PREF_USER_ID, "");
        String email = sharedPreferences.getString(Constants.SHARED_PREF_EMAIL, "");
        getUserUseCase.setUser(userId, email);
        Log.d(TAG, "getUser: aaaa" + email + " " + userId);
        getUserUseCase.execute(user -> {
            userMutableLiveData.setValue(user);
        }, throwable -> {
            Log.e(TAG, "getUser: ", throwable);
            userMutableLiveData.setValue(null);
        });
    }


    public void getARResponse() {
        Log.d(TAG, "getARResponse: getting user ar files...");
        getARPhotoOfUserUseCase.execute(s -> {
            Log.d(TAG, "getARResponse: successfully retrieved user files");
//            arFilesMutableLiveData.setValue((ArrayList<String>) s);
            Log.d(TAG, "getARResponse: " + s.getArMtl());
            Log.d(TAG, "getARResponse: " + s.getArObj());
            Log.d(TAG, "getARResponse: " + s.getArPng());
        }, throwable -> {
            Log.e(TAG, "getARResponse: " + throwable.getMessage());
        });
    }
}
