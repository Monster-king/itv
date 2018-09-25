package ru.surfstudio.itv.ui.base

import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter {

    protected val compositeDisposable = CompositeDisposable()

    abstract fun onCreate(savedInstanceState: Bundle?)

    open fun onSaveInstanceState(outState: Bundle) {
        // pass
    }

    open fun onDestroy() {
        compositeDisposable.dispose()
    }

}