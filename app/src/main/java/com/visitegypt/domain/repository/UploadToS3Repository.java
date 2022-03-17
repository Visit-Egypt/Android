package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.response.UploadedFilesResponse;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public interface UploadToS3Repository {
    Call<ResponseBody> uploadToS3(String url,
                                  RequestBody acl,
                                  RequestBody contentType,
                                  RequestBody key,
                                  RequestBody aws,
                                  RequestBody policy,
                                  RequestBody signature,
                                  MultipartBody.Part file);
}
