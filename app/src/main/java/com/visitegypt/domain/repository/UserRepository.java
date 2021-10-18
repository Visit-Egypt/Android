package com.visitegypt.domain.repository;
import com.visitegypt.domain.model.User;
import io.reactivex.Single;
import retrofit2.Call;

public interface UserRepository {
    /**
    void getUser();

    void deleteUser(User user);

    void registerUser(User user);
     *
     * /
     */
    Call<User> loginUser(User user);
}
