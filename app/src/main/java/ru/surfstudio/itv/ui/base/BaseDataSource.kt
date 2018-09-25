package ru.surfstudio.itv.ui.base

import android.arch.paging.PageKeyedDataSource
import java.util.concurrent.Executor

abstract class BaseDataSource<Key, Value>(private val retryExecutor: Executor) : PageKeyedDataSource<Key, Value>() {

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