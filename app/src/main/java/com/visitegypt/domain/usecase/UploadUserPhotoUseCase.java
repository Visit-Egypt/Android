package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.BASE_URL;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_REFRESH_TOKEN;

import android.content.SharedPreferences;
import android.util.Log;

import com.visitegypt.data.source.remote.ApiInterface;
import com.visitegypt.domain.model.PreSignedURL;
import com.visitegypt.domain.model.UploadFields;
import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.repository.UploadToS3Repository;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.utils.Constants;


import java.io.File;

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
//    private UploadToS3Repository uploadToS3Repository;
    private String contentType;
    private File userFile;
    ApiInterface apiInterface;

    @Inject
    public UploadUserPhotoUseCase(SharedPreferences sharedPreferences, @Named("Normal") UserRepository userRepository) {
        this.sharedPreferences = sharedPreferences;
        this.userRepository = userRepository;
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
        realUpload(awsUrl,uploadFields);

    }

    private void realUpload(String url, UploadFields uploadFields){
        if (userFile != null && userFile.exists()){

            Retrofit retrofit1 = new Retrofit.Builder()
                    .baseUrl("https://visitegypt-media-bucket.s3.amazonaws.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), userFile);
            RequestBody acl = RequestBody.create(MediaType.parse("text/plain"), uploadFields.getAcl());
            RequestBody contentType = RequestBody.create(MediaType.parse("text/plain"),uploadFields.getContentType());
            RequestBody key = RequestBody.create(uploadFields.getKey(),MediaType.parse("text/plain"));
            Log.d("TAG", "realUpload: key  " +key);
            RequestBody AWSAccessKeyId = RequestBody.create(MediaType.parse("text/plain"), uploadFields.getAWSAccessKeyId());
            RequestBody policy = RequestBody.create(MediaType.parse("text/plain"), uploadFields.getPolicy());
            RequestBody signature = RequestBody.create(MediaType.parse("text/plain"), uploadFields.getSignature());

            MultipartBody.Part rProfilePicture = MultipartBody.Part.createFormData("file", userFile.getName(), requestFile);
            ApiInterface apiInterface1 = retrofit1.create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface1.uploadToS3("", acl, contentType, key, AWSAccessKeyId, policy, signature, rProfilePicture);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("Upload:  suuuuuuu", response.toString());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //   Log.d(TAG, );
                    t.printStackTrace();
                }
            });

        }
    }

}

