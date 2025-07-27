package com.picpay.desafio.android.domain.repository

import com.picpay.desafio.android.domain.common.Resource
import com.picpay.desafio.android.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<Resource<List<User>>>
}