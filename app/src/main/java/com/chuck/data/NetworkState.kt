package com.chuck.data

sealed class NetworkState {
    object Success : NetworkState()
    object Loading : NetworkState()
    object Failed : NetworkState()
}
