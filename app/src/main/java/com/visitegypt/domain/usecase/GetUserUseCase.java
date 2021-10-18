package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

public  class GetUserUseCase {
    // TODO compare to https://github.com/ZahraHeydari/Android-Clean-Architecture-MVVM-Hilt-RX/blob/master/app/src/main/java/com/android/artgallery/domain/usecase/GetAlbumsUseCase.kt
    UserRepository userRepository;

    public GetUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}

