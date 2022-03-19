package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.BASE_URL;
import static com.visitegypt.utils.Constants.S3_URL;

import android.content.SharedPreferences;
import android.util.Log;

import com.visitegypt.data.source.remote.RetrofitServiceUpload;
import com.visitegypt.domain.model.ConfirmUploadModel;
import com.visitegypt.domain.model.ConfirmUploadResponse;
import com.visitegypt.domain.model.UploadFields;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.repository.UploadToS3Repository;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.utils.Constants;


import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.inject.Inject;
import javax.inject.Named;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UploadUserPhotoUseCase {
    private SharedPreferences sharedPreferences;
    private UserRepository userRepository;
    private String contentType;
    private File userFile;
    uploadPhotoApiCallBack uploadPhotoApiCallBack;

    public void setUploadPhotoApiCallBack(UploadUserPhotoUseCase.uploadPhotoApiCallBack uploadPhotoApiCallBack) {
        this.uploadPhotoApiCallBack = uploadPhotoApiCallBack;
    }

    UploadToS3Repository uploadToS3Repository;

    @Inject
    public UploadUserPhotoUseCase(SharedPreferences sharedPreferences, @Named("Normal") UserRepository userRepository, UploadToS3Repository uploadToS3Repository) {
        this.sharedPreferences = sharedPreferences;
        this.userRepository = userRepository;
        this.uploadToS3Repository = uploadToS3Repository;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUserFile(File userFile) {
        this.userFile = userFile;
    }

    public void upload() {
        final String userId = this.sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        final UploadResponse uploadResponse = userRepository.getPreSigendUrl(userId, contentType).blockingGet();
        UploadFields uploadFields = uploadResponse.getFields();
        realUpload(uploadFields, userId);

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
                            Log.d("TAG", "onResponse: status code  " + response.code());
                            uploadPhotoApiCallBack.confirmUpload(response.code());
                        }

                        @Override
                        public void onFailure(Call<ConfirmUploadResponse> call, Throwable t) {

                        }
                    });


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("TAG", "onFailure: ");
                }
            });

        }
    }

    public interface uploadPhotoApiCallBack {
        void confirmUpload(int code);
    }

}