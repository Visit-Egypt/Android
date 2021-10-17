package com.visitegypt.domain.usecase.base;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class SingleUseCase<T> extends UseCase {
    protected abstract Single buildSingleUseCase();

    void execute() {
        disposeLast();
        lastDisposable = buildSingleUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}
