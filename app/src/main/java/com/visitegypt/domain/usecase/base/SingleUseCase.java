package com.visitegypt.domain.usecase.base;

import io.reactivex.rxjava3.core.Single;

public abstract class SingleUseCase<T> extends UseCase {
    protected abstract Single buildSingleUseCase();

    void execute() {
    }

}
