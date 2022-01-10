package com.visitegypt.domain.repository;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.response.UploadFields;
import com.visitegypt.domain.model.response.UploadResponse;

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
    Single<User> refreshUserToken(User user);
    Single<User> getUser(String userId,String email);
    Single<UploadResponse> uploadUserPhoto(String userId, String contentType);
    Single<ResponseBody> genericUpload(String uploadUrl, UploadFields uploadFields, MultipartBody.Part file);
}
