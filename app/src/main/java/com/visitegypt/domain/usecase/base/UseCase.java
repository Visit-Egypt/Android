package com.visitegypt.domain.usecase.base;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class UseCase {
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    Disposable lastDisposable = null;

    protected void setDisposable(Disposable lastDisposable) {
        this.lastDisposable = lastDisposable;
    }

    void disposeLast() {
        if (lastDisposable != null) {
            if (!lastDisposable.isDisposed()) {
                lastDisposable.dispose();
            }
        }
    }

    void dispose() {
        compositeDisposable.clear();
    }
}