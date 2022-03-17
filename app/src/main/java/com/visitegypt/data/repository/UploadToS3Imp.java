package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.data.source.remote.RetrofitServiceUpload;
import com.visitegypt.domain.model.response.UploadedFilesResponse;
import com.visitegypt.domain.repository.UploadToS3Repository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
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
    public Call<ResponseBody> uploadToS3(String url, RequestBody acl, RequestBody contentType, RequestBody key, RequestBody aws, RequestBody policy, RequestBody signature, MultipartBody.Part file) {
        return retrofitServiceUpload.uploadToS3("", acl, contentType, key, aws, policy, signature, file);
    }
}

