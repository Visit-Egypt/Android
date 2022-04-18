package com.visitegypt.presentation.post;

import static com.visitegypt.utils.Constants.SHARED_PREF_FIRST_NAME;
import static com.visitegypt.utils.Constants.SHARED_PREF_LAST_NAME;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_IMAGE;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.usecase.AddNewPostUseCase;
import com.visitegypt.domain.usecase.UpdateBadgeOfUserUseCase;
import com.visitegypt.domain.usecase.UpdateUserBadgeTaskProgUseCase;
import com.visitegypt.domain.usecase.UpdateUserPlaceActivityUseCase;
import com.visitegypt.domain.usecase.UploadUserPhotoUseCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PostsViewModel extends ViewModel implements UploadUserPhotoUseCase.uploadPhotoApiCallBack {
    private static final String TAG = "Posts ViewModel";

    MutableLiveData<List<PlaceActivity>> placeActivitiesMutableLiveData = new MutableLiveData<>();
    private AddNewPostUseCase addNewPostUseCase;
    private UpdateUserPlaceActivityUseCase updateUserPlaceActivityUseCase;
    private UploadUserPhotoUseCase uploadUserPhotoUseCase;
    private UpdateBadgeOfUserUseCase updateBadgeOfUserUseCase;

    SharedPreferences sharedPreferences;
    private UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase;
    private List<String> imageList = new ArrayList<>();
    private PlaceActivity postPlaceActivity;
    private Post post;

    MutableLiveData<List<Badge>> badgesMutableLiveData = new MutableLiveData<>();
    private Badge badge;
    MutableLiveData<Boolean> isImageUploaded = new MutableLiveData<>();
    MutableLiveData<Boolean> isPostUploaded = new MutableLiveData<>();

    @Inject
    public PostsViewModel(AddNewPostUseCase addNewPostUseCase,
                          SharedPreferences sharedPreferences,
                          UploadUserPhotoUseCase uploadUserPhotoUseCase,
                          UpdateBadgeOfUserUseCase updateBadgeOfUserUseCase,
                          UpdateUserPlaceActivityUseCase updateUserPlaceActivityUseCase,
                          UpdateUserBadgeTaskProgUseCase updateUserBadgeTaskProgUseCase) {
        this.addNewPostUseCase = addNewPostUseCase;
        this.sharedPreferences = sharedPreferences;
        this.uploadUserPhotoUseCase = uploadUserPhotoUseCase;
        this.updateBadgeOfUserUseCase = updateBadgeOfUserUseCase;
        this.updateUserPlaceActivityUseCase = updateUserPlaceActivityUseCase;
        this.updateUserBadgeTaskProgUseCase = updateUserBadgeTaskProgUseCase;
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
            Log.d(TAG, "addPost: success");
            isPostUploaded.setValue(true);

            Log.d(TAG, "addPost: updating badge");
            updateUserBadge();

            Log.d(TAG, "addPost: updating post activity");
            updatePlaceActivity();
        }, throwable -> {
            Log.d(TAG, "addPost: error" + throwable.getMessage());
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

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    private void updateUserBadge() {
        for (BadgeTask badgeTask : badge.getBadgeTasks()) {
            if (badgeTask.getType().equals(BadgeTask.SOCIAL_BUTTERFLY)) {
                if (badgeTask.getProgress() < badgeTask.getMaxProgress())
                    badgeTask.setProgress(badgeTask.getProgress() + 1);
                else
                    Log.d(TAG, "updateUserBadge: already earned the badge");
                updateUserBadgeTaskProgUseCase.setBadgeTask(badgeTask);
                updateUserBadgeTaskProgUseCase.execute(badgeTasks -> {

                }, throwable -> {

                });
            }
        }
        updateBadgeOfUserUseCase.setBadge(badge);
        updateBadgeOfUserUseCase.execute(badges -> {
            badgesMutableLiveData.setValue(badges);
        }, throwable -> {

        });
    }

    private void updatePlaceActivity() {
        if (postPlaceActivity.getProgress() < postPlaceActivity.getMaxProgress()) {
            postPlaceActivity.setProgress(postPlaceActivity.getProgress() + 1);
            if (postPlaceActivity.getProgress() == postPlaceActivity.getMaxProgress()) {
                postPlaceActivity.setFinished(true);
            }
            updateUserPlaceActivityUseCase.setPlaceActivity(postPlaceActivity);
            updateUserPlaceActivityUseCase.execute(placeActivities -> placeActivitiesMutableLiveData.setValue(placeActivities),
                    throwable -> {
                        Log.e(TAG, "updatePlaceActivity: " + throwable.getMessage());
                    });
        } else {
            Log.d(TAG, "updatePlaceActivity: already granted");
        }
    }

    public String getUserImage() {
        return sharedPreferences.getString(SHARED_PREF_USER_IMAGE, "");
    }

    public void setPostPlaceActivity(PlaceActivity postPlaceActivity) {
        this.postPlaceActivity = postPlaceActivity;
    }
}
