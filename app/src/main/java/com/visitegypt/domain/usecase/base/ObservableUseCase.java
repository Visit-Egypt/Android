package com.visitegypt.domain.usecase.base;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public abstract class ObservableUseCase<T> extends UseCase {
    protected abstract Observable<T> buildObservableUseCase();

    public void execute(Consumer<T> onSuccess, Consumer<Throwable> onError) {
        disposeLast();
        lastDisposable = buildObservableUseCase()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError);
        compositeDisposable.add(lastDisposable);
    }

    public void executeWithConcat(Consumer<T> onSuccess,
                                  Consumer<Throwable> onError,
                                  Observable<T> nextObservable) {
        disposeLast();
        lastDisposable = buildObservableUseCase()
                .concatWith(nextObservable)
                .subscribe(onSuccess, onError);
        compositeDisposable.add(lastDisposable);
    }
}
