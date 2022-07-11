package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.S3_URL;

import android.content.SharedPreferences;
import android.util.Log;

import com.visitegypt.domain.model.ConfirmUploadModel;
import com.visitegypt.domain.model.ConfirmUploadResponse;
import com.visitegypt.domain.model.UploadFields;
import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.repository.UploadToS3Repository;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadUserPhotoUseCase {
    private static final String TAG = "upload user photo use case";
    uploadPhotoApiCallBack uploadPhotoApiCallBack;
    UploadToS3Repository uploadToS3Repository;
    private SharedPreferences sharedPreferences;
    private UserRepository userRepository;
    private String contentType;
    private File userFile;

    @Inject
    public UploadUserPhotoUseCase(SharedPreferences sharedPreferences, @Named("Normal") UserRepository userRepository, UploadToS3Repository uploadToS3Repository) {
        this.sharedPreferences = sharedPreferences;
        this.userRepository = userRepository;
        this.uploadToS3Repository = uploadToS3Repository;
    }

    public void setUploadPhotoApiCallBack(UploadUserPhotoUseCase.uploadPhotoApiCallBack uploadPhotoApiCallBack) {
        this.uploadPhotoApiCallBack = uploadPhotoApiCallBack;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUserFile(File userFile) {
        this.userFile = userFile;
    }

    public void upload() {
        final String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        Log.d(TAG, "upload: " + userId);
        try {
            final UploadResponse uploadResponse = userRepository.getPreSignedUrl(userId, contentType).blockingGet();
            UploadFields uploadFields = uploadResponse.getFields();
            realUpload(uploadFields, userId);
        } catch (Exception e) {
            Log.d(TAG, "upload: " + e.toString());
        }

    }

    public void uploadARPhoto() {
        final String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        Log.d(TAG, "upload: " + userId);
        try {
            final UploadResponse uploadResponse = userRepository.getUserPhotoAR(userId, contentType).blockingGet();
            UploadFields uploadFields = uploadResponse.getFields();
            realUpload(uploadFields, userId);
        } catch (Exception e) {
            Log.e(TAG, "upload: " + e.toString());
        }
    }

    private void realUpload(UploadFields uploadFields, String userId) {
        if (userFile != null && userFile.exists()) {
            ArrayList<String> imagesKes = new ArrayList<>();

            ArrayList<String> errorKes = new ArrayList<>();
            Call<ResponseBody> call = uploadToS3Repository.uploadToS3(userFile, uploadFields);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 204) {
                        imagesKes.clear();
                        errorKes.clear();
                        imagesKes.add(S3_URL + uploadFields.getKey());

                    } else {

                    }
                    ConfirmUploadModel confirmUploadModel = new ConfirmUploadModel(imagesKes, userId);
                    Call<ConfirmUploadResponse> confirmCall = userRepository.confirmUpload(confirmUploadModel);
                    confirmCall.enqueue(new Callback<ConfirmUploadResponse>() {
                        @Override
                        public void onResponse(Call<ConfirmUploadResponse> call, Response<ConfirmUploadResponse> response) {
                            Log.d(TAG, "onResponse: status code  " + response.code());
                            uploadPhotoApiCallBack.confirmUpload(response.code(), imagesKes);
                        }

                        @Override
                        public void onFailure(Call<ConfirmUploadResponse> call, Throwable t) {

                        }
                    });


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "onFailure: ");
                }
            });

        }
    }

    public interface uploadPhotoApiCallBack {
        void confirmUpload(int code, List<String> url);
    }

}