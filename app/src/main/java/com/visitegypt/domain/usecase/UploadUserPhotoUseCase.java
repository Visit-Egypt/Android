package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.BASE_URL;

import android.content.SharedPreferences;
import android.util.Log;

import com.visitegypt.data.source.remote.RetrofitServiceUpload;
import com.visitegypt.domain.model.UploadFields;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.repository.UploadToS3Repository;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.utils.Constants;


import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import javax.inject.Inject;
import javax.inject.Named;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class UploadUserPhotoUseCase{
    private SharedPreferences sharedPreferences;
    private UserRepository userRepository;
    private String contentType;
    private File userFile;
    UploadToS3Repository uploadToS3Repository;

    @Inject
    public UploadUserPhotoUseCase(SharedPreferences sharedPreferences, @Named("Normal") UserRepository userRepository,UploadToS3Repository uploadToS3Repository) {
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
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), userFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", userFile.getName(), requestFile);
        final String userId = this.sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        final UploadResponse uploadResponse = userRepository.getPreSigendUrl(userId, contentType).blockingGet();
        String awsUrl = uploadResponse.getUrl();
        UploadFields uploadFields = uploadResponse.getFields();
        Log.d("TAG", "upload: url  "+awsUrl);
        realUpload(awsUrl,uploadFields);

    }

    private void realUpload(String url, UploadFields uploadFields){
        if (userFile != null && userFile.exists()){

            RequestBody requestFile = RequestBody.create( userFile,MediaType.parse("multipart/form-data"));
            RequestBody acl = RequestBody.create(uploadFields.getAcl(),MediaType.parse("text/plain"));
            RequestBody contentType = RequestBody.create(uploadFields.getContentType(),MediaType.parse("text/plain"));
            RequestBody key = RequestBody.create(MediaType.parse("text/plain"),uploadFields.getKey());
            Log.d("TAG", "realUpload: key  " +key);
            RequestBody AWSAccessKeyId = RequestBody.create( uploadFields.getAWSAccessKeyId(),MediaType.parse("text/plain"));
            RequestBody policy = RequestBody.create(uploadFields.getPolicy(),MediaType.parse("text/plain"));
            RequestBody signature = RequestBody.create(uploadFields.getSignature(),MediaType.parse("text/plain"));
            MultipartBody.Part rProfilePicture = MultipartBody.Part.createFormData("file", userFile.getName(), requestFile);
            Call<ResponseBody> call  = uploadToS3Repository.uploadToS3("", acl, contentType, key, AWSAccessKeyId, policy, signature, rProfilePicture);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("TAG", "onResponse: " + response.code());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("TAG", "onFailure: ");
                }
            });

//            Call<ResponseBody> call = retrofitServiceUpload1.uploadToS3("", acl, contentType, key, AWSAccessKeyId, policy, signature, rProfilePicture);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    Log.d("Upload:  suuuuuuu", response.toString());
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    //   Log.d(TAG, );
//                    t.printStackTrace();
//                }
//            });

        }
    }

}