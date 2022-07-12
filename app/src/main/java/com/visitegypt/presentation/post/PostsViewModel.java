package com.visitegypt.presentation.post;

import static com.visitegypt.utils.Constants.SHARED_PREF_FIRST_NAME;
import static com.visitegypt.utils.Constants.SHARED_PREF_LAST_NAME;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_IMAGE;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.XPUpdate;
import com.visitegypt.domain.usecase.AddNewPostUseCase;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.UpdatePostAPostActivityUseCase;
import com.visitegypt.domain.usecase.UploadUserPhotoUseCase;
import com.visitegypt.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PostsViewModel extends ViewModel implements UploadUserPhotoUseCase.uploadPhotoApiCallBack {
    private static final String TAG = "Posts ViewModel";

    private AddNewPostUseCase addNewPostUseCase;

    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> isImageUploaded = new MutableLiveData<>();
    MutableLiveData<Boolean> isPostUploaded = new MutableLiveData<>();
    MutableLiveData<XPUpdate> postUpdatedXP = new MutableLiveData<>();

    private UploadUserPhotoUseCase uploadUserPhotoUseCase;
    private UpdatePostAPostActivityUseCase updatePostAPostActivityUseCase;
    private GetUserUseCase getUserUseCase;

    private SharedPreferences sharedPreferences;

    private List<String> imageList = new ArrayList<>();
    private Post post;

    @Inject
    public PostsViewModel(AddNewPostUseCase addNewPostUseCase,
                          SharedPreferences sharedPreferences,
                          UploadUserPhotoUseCase uploadUserPhotoUseCase,
                          UpdatePostAPostActivityUseCase updatePostAPostActivityUseCase,
                          GetUserUseCase getUserUseCase) {
        this.addNewPostUseCase = addNewPostUseCase;
        this.sharedPreferences = sharedPreferences;
        this.uploadUserPhotoUseCase = uploadUserPhotoUseCase;
        this.updatePostAPostActivityUseCase = updatePostAPostActivityUseCase;
        this.getUserUseCase = getUserUseCase;
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

    private void updateUserPostActivity() {
        updatePostAPostActivityUseCase.setPlaceId(post.getPlaceId());
        updatePostAPostActivityUseCase.execute(xpUpdate -> {
            postUpdatedXP.setValue(xpUpdate);
            Log.d(TAG, "updateUserPostActivity: updated post activity");
        }, throwable -> {
            postUpdatedXP.setValue(null);
            Log.e(TAG, "updateUserPostActivity: " + throwable.getMessage());
        });
    }

    public void addPost() {
        addNewPostUseCase.execute(post1 -> {
            Log.d(TAG, "addPost: success");
            isPostUploaded.setValue(true);
            Log.d(TAG, "addPost: updating badge");
            Log.d(TAG, "addPost: updating post activity");
            updateUserPostActivity();
        }, throwable -> {
            Log.e(TAG, "addPost: error" + throwable.getMessage());
        });
    }

    public void uploadUserProfilePhoto(File userPhoto, String contentType) {
        uploadUserPhotoUseCase.setContentType(contentType);
        uploadUserPhotoUseCase.setUserFile(userPhoto);
        uploadUserPhotoUseCase.upload();
    }

    public void initCallBack() {
        uploadUserPhotoUseCase.setUploadPhotoApiCallBack(this::confirmUpload);
    }

    @Override
    public void confirmUpload(int code, List<String> url) {
        if (code == 200) {
            isImageUploaded.setValue(true);
            imageList = url;
        }
    }


    public String getUserImage() {
        return sharedPreferences.getString(SHARED_PREF_USER_IMAGE, "");
    }

    public void getUser() {
        String userId = sharedPreferences.getString(SHARED_PREF_USER_ID, "");
        String email = sharedPreferences.getString(Constants.SHARED_PREF_EMAIL, "");
        getUserUseCase.setUser(userId, email);
        getUserUseCase.execute(user -> {
            userMutableLiveData.setValue(user);
        }, throwable -> {
            Log.e(TAG, "getUser: ", throwable);
            userMutableLiveData.setValue(null);
        });
    }

}
