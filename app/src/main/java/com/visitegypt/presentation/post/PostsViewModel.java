package com.visitegypt.presentation.post;

import static com.visitegypt.utils.Constants.SHARED_PREF_FIRST_NAME;
import static com.visitegypt.utils.Constants.SHARED_PREF_LAST_NAME;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.usecase.AddNewPostUseCase;
import com.visitegypt.domain.usecase.GetAllCitiesUseCase;
import com.visitegypt.domain.usecase.GetPlacesOfCityUseCase;
import com.visitegypt.domain.usecase.UploadUserPhotoUseCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PostsViewModel extends ViewModel implements UploadUserPhotoUseCase.uploadPhotoApiCallBack {
    private static final String TAG = "Posts ViewModel";
    AddNewPostUseCase addNewPostUseCase;
    SharedPreferences sharedPreferences;
    private Post post;
    private List<String> imageList = new ArrayList<>();
    private UploadUserPhotoUseCase uploadUserPhotoUseCase;
    MutableLiveData<Boolean> isImageUploaded = new MutableLiveData<>();
    MutableLiveData<Boolean> isPostUploaded = new MutableLiveData<>();

    @Inject
    public PostsViewModel(AddNewPostUseCase addNewPostUseCase, SharedPreferences sharedPreferences, UploadUserPhotoUseCase uploadUserPhotoUseCase) {
        this.addNewPostUseCase = addNewPostUseCase;
        this.sharedPreferences = sharedPreferences;
        this.uploadUserPhotoUseCase = uploadUserPhotoUseCase;
    }

    public void createFakePost() {
        List<String> images = new ArrayList<>();
        String placeId = "616f2746b817807a7a6c7167";
        String userId = "617170dacb2e775f16fc54f2";
        String userName = "Yehia Hendy";
        List<String> likes = new ArrayList<>();
        post = new Post("This is try post", images, placeId, userId, userName, likes);
        addNewPostUseCase.setPost(post);
    }

    public void setPost(Post post) {
        this.post = post;
    }


    public void setPostData(String caption, String placeID) {
        String userId = sharedPreferences.getString(SHARED_PREF_USER_ID, "");
        String userName = sharedPreferences.getString(SHARED_PREF_FIRST_NAME, "") + " " +
                sharedPreferences.getString(SHARED_PREF_LAST_NAME, "");
        post = new Post(caption, imageList, placeID, userId, userName);
        addNewPostUseCase.setPost(post);
    }

    public void addPost() {
        addNewPostUseCase.execute(post1 -> {
            isPostUploaded.setValue(true);
        }, throwable -> {
            Log.d(TAG, "addPost: error" + throwable.getMessage());
        });
    }

    public void uploadUserProfilePhoto(File userPhoto, String contentType) {
        uploadUserPhotoUseCase.setContentType(contentType);
        uploadUserPhotoUseCase.setUserFile(userPhoto);
        uploadUserPhotoUseCase.upload();

    }
    public void initCallBack()
    {
        uploadUserPhotoUseCase.setUploadPhotoApiCallBack(this::confirmUpload);
    }
    @Override
    public void confirmUpload(int code, List<String> url) {
        if (code == 200) {
            isImageUploaded.setValue(true);
            imageList = url;
        }

    }
}