package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.repository.NotificationRepository;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class NotificationRepositoryImp implements NotificationRepository {
    RetrofitService retrofitService;

    @Inject
    public NotificationRepositoryImp(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public Single<HashMap<Object, Object>> RegisterDeviceToNotification(String token) {
        HashMap<Object,Object> map = new HashMap<>();
        map.put("device_token",token);
        return retrofitService.RegisterDeviceToNotification(map);
    }
}
