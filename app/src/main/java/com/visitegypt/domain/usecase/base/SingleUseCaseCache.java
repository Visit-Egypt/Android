package com.visitegypt.domain.usecase.base;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class SingleUseCaseCache<T>  extends UseCase{
    protected abstract Single<T> buildSingleUseCase();
    public void execute(Consumer<T> onSuccess, Consumer<Throwable> onError) {
        disposeLast();
        lastDisposable = buildSingleUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()

                .subscribe(onSuccess, onError);
        compositeDisposable.add(lastDisposable);
    }
}
