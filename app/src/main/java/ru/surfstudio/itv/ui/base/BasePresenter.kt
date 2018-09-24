package ru.surfstudio.itv.ui.base

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter {

    protected val compositeDisposable = CompositeDisposable()

    abstract fun onCreate()

    open fun onDestroy() {
        compositeDisposable.dispose()
    }

}