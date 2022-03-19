package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.ConfirmUploadModel;
import com.visitegypt.domain.model.ConfirmUploadResponse;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.model.response.UploadResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public interface UserRepository {
    /**
     * void getUser();
     * <p>
     * void deleteUser(User user);
     * <p>
     * <p>
     * /
     *
     * @return
     */
    Single<User> registerUser(User user);

    Single<User> loginUser(User user);

    Single<User> refreshUserToken(Token token);

    Single<User> getUser(String userId, String email);

    Single<String> logOut(String userId);

    Single<User> updateUser(String userId, UserUpdateRequest user);

    Single<UploadResponse> getPreSigendUrl(String userId, String contentType);
    Call<ConfirmUploadResponse> confirmUpload(ConfirmUploadModel confirmUploadModel);

}
