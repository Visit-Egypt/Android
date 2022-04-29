package com.visitegypt.domain.usecase.base;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;

public abstract class CompletableUseCase<T> extends UseCase {
    protected abstract Completable buildCompletableUseCase();

    public void execute(Action onComplete, Consumer<Throwable> onError) {
        disposeLast();
        lastDisposable = buildCompletableUseCase()
                .subscribe(onComplete, onError);

        compositeDisposable.add(lastDisposable);
    }

    public void executeWithConcat(Action onComplete,
                                  Consumer<Throwable> onError,
                                  Completable completable) {
        disposeLast();
        lastDisposable = buildCompletableUseCase()
                .concatWith(completable)
                .subscribe(onComplete, onError);
        compositeDisposable.add(lastDisposable);
    }
}
