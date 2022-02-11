package com.visitegypt.domain.usecase;

import android.content.SharedPreferences;
import android.util.Log;

import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.model.response.UploadedFilesResponse;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;
import com.visitegypt.utils.Constants;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class UploadUserPhotoUseCase extends SingleUseCase<UploadedFilesResponse> {
    private SharedPreferences sharedPreferences;
    private UserRepository userRepository;
    private String contentType;
    private File userFile;
    @Inject
    public UploadUserPhotoUseCase (UserRepository userRepository, SharedPreferences sharedPreferences){
        this.userRepository = userRepository;
        this.sharedPreferences = sharedPreferences;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public void setUserFile(File userFile) {
        this.userFile = userFile;
    }
    @Override
    protected Single<UploadedFilesResponse> buildSingleUseCase() {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), userFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", userFile.getName(), requestFile);

        final String userId = this.sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        final UploadResponse uploadResponse = userRepository.uploadUserPhoto(userId, contentType).blockingGet();

        // RequestBody requestFields = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), );
        // Upload Photo to the server.
        userRepository.genericUpload(uploadResponse.getUrl(), uploadResponse.getFields(), body).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody response) throws Throwable {
                // if(response.code() == 204){
                // Successfull Upload
                Log.d("upload: suu", String.valueOf(response.string()));

            }
        });
        return null;
    }
}
