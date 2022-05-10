package com.visitegypt.domain.repository;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Single;

public interface NotificationRepository {
    public Single<HashMap<Object,Object>> RegisterDeviceToNotification(String token);
}
