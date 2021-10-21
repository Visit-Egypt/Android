package com.visitegypt.domain.repository;
import com.visitegypt.domain.model.User;

import io.reactivex.rxjava3.core.Single;
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

}
