package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.model.response.UploadFields;
import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.repository.UserRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class UserRepositoryImp implements UserRepository {

    private RetrofitService retrofitService;

    @Inject
    public UserRepositoryImp(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public Single<User> registerUser(User user) {
        return retrofitService.registerUser(user);
    }

    @Override
    public Single<User> loginUser(User user) {
        return retrofitService.loginUser(user);
    }

    @Override
    public Single<User> refreshUserToken(Token token) {
        return retrofitService.refreshUserToken(token);
    }


    @Override
    public Single<User> getUser(String userId, String email) {
        return retrofitService.getUser(userId,email);
    }

    @Override
    public Single<User> updateUser(String userId, UserUpdateRequest user) {
        return retrofitService.updateUser(userId, user);
    }

    @Override
    public Single<UploadResponse> uploadUserPhoto(String userId, String contentType){
        return retrofitService.uploadUserPhoto(userId, contentType);
    }

    @Override
    public Single<ResponseBody> genericUpload (String uploadUrl, UploadFields uploadFields, MultipartBody.Part file){
        return retrofitService.genericUpload(uploadUrl, uploadFields, file);
    }
}