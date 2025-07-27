package com.picpay.desafio.android.data.remote

import com.picpay.desafio.android.data.remote.dto.UserDto
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<UserDto>
}