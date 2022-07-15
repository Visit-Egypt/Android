package com.visitegypt.domain.usecase;

import com.visitegypt.domain.repository.NotificationRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class RegisterDeviceToNotificationUseCase extends SingleUseCase<HashMap<Object, Object>> {
    NotificationRepository notificationRepository;
    private String msg;

    @Inject
    public RegisterDeviceToNotificationUseCase(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    protected Single<HashMap<Object, Object>> buildSingleUseCase() {

        return notificationRepository.RegisterDeviceToNotification(msg);
    }
}
