package ru.surfstudio.itv.ui.base

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.ViewGroup
import android.widget.FrameLayout

abstract class BaseView(context: Context) : FrameLayout(context) {

    fun inflate(@LayoutRes id: Int) {
        inflate(context, id, this)
    }

}