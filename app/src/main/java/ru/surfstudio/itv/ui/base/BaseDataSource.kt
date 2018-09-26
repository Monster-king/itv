package ru.surfstudio.itv.ui.base

import android.arch.paging.PositionalDataSource
import java.util.concurrent.Executor

abstract class BaseDataSource<Value>(private val retryExecutor: Executor) : PositionalDataSource<Value>() {

    protected var retry: (() -> Any)? = null

    fun retryLoad() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

}