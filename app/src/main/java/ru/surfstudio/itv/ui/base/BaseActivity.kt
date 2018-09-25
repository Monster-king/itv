package ru.surfstudio.itv.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var view: BaseViewInterface<View>

    @Inject
    lateinit var presenter: BasePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.view)
        presenter.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState ?: return)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}