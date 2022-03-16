package com.visitegypt.data.source.remote;

import com.visitegypt.domain.model.PreSignedURL;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {
    @GET("api/user/{id}/upload-photo")
    Call<PreSignedURL> getURL (@Path("id") String id, @Query("content_type") String contentType);

    @Multipart
    @POST
    Call<ResponseBody> uploadToS3(
                    @Url String url,
                    @Part("acl") RequestBody acl,
                    @Part("Content-Type") RequestBody content_type,
                    @Part("key") RequestBody key,
                    @Part("AWSAccessKeyId") RequestBody aws,
                    @Part("policy") RequestBody policy,
                    @Part("signature") RequestBody signature,
                    @Part MultipartBody.Part file);
}
