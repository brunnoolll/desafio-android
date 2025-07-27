package com.picpay.desafio.android.domain.common

import com.picpay.desafio.android.domain.exception.ApiException

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val error: ApiException) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}