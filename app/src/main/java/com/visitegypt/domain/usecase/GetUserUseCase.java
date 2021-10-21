package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.User;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import io.reactivex.rxjava3.core.Single;

public class GetUserUseCase extends SingleUseCase<User> {
    @Override
    protected Single<User> buildSingleUseCase() {
        return null;
    }
}
