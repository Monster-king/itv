package ru.surfstudio.itv.network.pojo

class BaseResponse<T> (
        val page: Int,
        val results: List<T>
)