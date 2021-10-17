package com.visitegypt.domain.usecase.base;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class SingleUseCase<T extends Object> extends UseCase {
    protected abstract Single<T> buildSingleUseCase();

    public void execute(Consumer onSuccess, Consumer onError) {
        disposeLast();
        lastDisposable = buildSingleUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError);
        compositeDisposable.add(lastDisposable);
    }

}
