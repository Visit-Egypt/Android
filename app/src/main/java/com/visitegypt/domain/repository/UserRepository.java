package com.visitegypt.domain.repository;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.model.response.UploadFields;
import com.visitegypt.domain.model.response.UploadResponse;

import javax.inject.Named;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

public interface UserRepository {
    /**
    void getUser();

    void deleteUser(User user);

     *
     * /
     * @return
     */
    Single<User> registerUser(User user);
    Single<User> loginUser(User user);
    Single<User> refreshUserToken(Token token);
    Single<User> getUser(String userId,String email);
    Single<String> logOut(String userId);
    Single<User> updateUser(String userId, UserUpdateRequest user);
    Single<UploadResponse> uploadUserPhoto(String userId, String contentType);
    Single<ResponseBody> genericUpload(String uploadUrl, UploadFields uploadFields, MultipartBody.Part file);
}
