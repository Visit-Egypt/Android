package com.visitegypt.data.repository;

import android.util.Log;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.data.source.remote.RetrofitServiceUpload;
import com.visitegypt.domain.model.UploadFields;
import com.visitegypt.domain.model.response.UploadedFilesResponse;
import com.visitegypt.domain.repository.UploadToS3Repository;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class UploadToS3Imp implements UploadToS3Repository  {
    private RetrofitServiceUpload retrofitServiceUpload;
    @Inject
    public UploadToS3Imp(RetrofitServiceUpload retrofitServiceUpload) {
        this.retrofitServiceUpload = retrofitServiceUpload;
    }


    @Override
    public Call<ResponseBody> uploadToS3(File file, UploadFields uploadFields) {

        RequestBody requestFile = RequestBody.create( file, MediaType.parse("multipart/form-data"));
        RequestBody acl = RequestBody.create(uploadFields.getAcl(),MediaType.parse("text/plain"));
        RequestBody contentType = RequestBody.create(uploadFields.getContentType(),MediaType.parse("text/plain"));
        RequestBody key = RequestBody.create(MediaType.parse("text/plain"),uploadFields.getKey());
        Log.d("TAG", "realUpload: key  " +key);
        RequestBody aws = RequestBody.create( uploadFields.getAWSAccessKeyId(),MediaType.parse("text/plain"));
        RequestBody policy = RequestBody.create(uploadFields.getPolicy(),MediaType.parse("text/plain"));
        RequestBody signature = RequestBody.create(uploadFields.getSignature(),MediaType.parse("text/plain"));
        MultipartBody.Part rProfilePicture = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return retrofitServiceUpload.uploadToS3("", acl, contentType, key, aws, policy, signature, rProfilePicture);
    }
}

