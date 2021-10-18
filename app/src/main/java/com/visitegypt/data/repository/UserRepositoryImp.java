package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.di.NetworkModule;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;

import io.reactivex.Single;
import retrofit2.Call;

public class UserRepositoryImp implements UserRepository {
    NetworkModule networkModule = new NetworkModule();
    @Override
    public Call<User> loginUser(User user) {
        return networkModule.getINSTANCE().getRetrofitService().registerUser(user) ;
    }
}
