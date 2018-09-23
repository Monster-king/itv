package ru.surfstudio.itv.network

sealed class NetworkState
object Loading : NetworkState()
object Loaded : NetworkState()
data class Failed(val message: String) : NetworkState()
