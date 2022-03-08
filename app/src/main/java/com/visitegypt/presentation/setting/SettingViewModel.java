package com.visitegypt.presentation.setting;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.usecase.GetUserUseCase;
import com.visitegypt.domain.usecase.LogOutUseCase;
import com.visitegypt.domain.usecase.UpdateUserUseCase;
import com.visitegypt.utils.Constants;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class SettingViewModel extends ViewModel {
    private static final String TAG = "Setting View Model";
    GetUserUseCase getUserUseCase;
    UpdateUserUseCase updateUserUseCase;
    LogOutUseCase logOutUseCase;
    SharedPreferences sharedPreferences;
    MutableLiveData<Boolean> isLoged = new MutableLiveData<>();
    public MutableLiveData<User> mutableLiveDataUser = new MutableLiveData<>();
    @Inject
    public SettingViewModel(GetUserUseCase getUserUseCase, SharedPreferences sharedPreferences,UpdateUserUseCase updateUserUseCase,LogOutUseCase logOutUseCase) {
        this.getUserUseCase = getUserUseCase;
        this.sharedPreferences = sharedPreferences;
        this.updateUserUseCase = updateUserUseCase;
        this.logOutUseCase = logOutUseCase;
        // this.uploadUserPhotoUseCase = uploadUserPhotoUseCase;

    }

    public void getUserData(){

        getUserUseCase.setUser(sharedPreferences.getString(SHARED_PREF_USER_ID,null),
                sharedPreferences.getString(Constants.SHARED_PREF_EMAIL,null));
        getUserUseCase.execute(new Consumer<User>() {
            @Override
            public void accept(User user) throws Throwable {
                Log.d("TAG", "onChanged: setting ++" + user.getFirstName());
                mutableLiveDataUser.setValue(user);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                try {
                    ResponseBody body = ((HttpException) throwable).response().errorBody();
                    JSONObject jObjectError = new JSONObject(body.string());
                    Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
                    if (jObjectError.getJSONArray("errors").toString().contains("msg")) {


                    } else {

                    }
                } catch (Exception e) {
                    Log.d("TAG", "accept catch: " + e.toString());
                }
            }
        });
    }

    /*
        public void uploadUserProfilePhoto(File userPhoto, String contentType){
            uploadUserPhotoUseCase.setContentType(contentType);
            uploadUserPhotoUseCase.setUserFile(userPhoto);
            uploadUserPhotoUseCase.execute(new Consumer<UploadedFilesResponse>() {
                @Override
                public void accept(UploadedFilesResponse uploadedFilesResponse) throws Throwable {
                    mutableLiveDataUploadedFiles.setValue(uploadedFilesResponse);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Throwable {
                    error.setValue(throwable.toString());
                }
            });
        }
        */
    public void logOut()
    {
       logOutUseCase.setUserId(sharedPreferences.getString(SHARED_PREF_USER_ID,null));
       logOutUseCase.execute(s -> {
           sharedPreferences.edit().clear().commit();
           isLoged.setValue(false);
       },throwable -> {
           Log.d(TAG, "logOut: " + throwable.getMessage());

       });
    }
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        updateUserUseCase.setUser(userUpdateRequest);
        updateUserUseCase.execute(new Consumer<User>() {
            @Override
            public void accept(User user) throws Throwable {
                saveNewData(user);
                mutableLiveDataUser.setValue(user);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                try {
                    ResponseBody body = ((HttpException) throwable).response().errorBody();
                    JSONObject jObjectError = new JSONObject(body.string());
                    Log.d("TAG", "accept try : " + jObjectError.getJSONArray("errors").toString());
                    if (jObjectError.getJSONArray("errors").toString().contains("msg")) {


                    } else {

                    }
                } catch (Exception e) {
                    Log.d("TAG", "accept catch: " + e.toString());
                }
            }
        });

    }
    public void saveNewData(User user)
    {
        sharedPreferences.edit()
                .putString(SHARED_PREF_USER_ID, user.getUserId())
                .putString(Constants.SHARED_PREF_FIRST_NAME, user.getFirstName() + " " + user.getLastName())
                .apply();
    }
}
