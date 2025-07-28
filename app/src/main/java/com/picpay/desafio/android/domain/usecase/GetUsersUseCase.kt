package com.picpay.desafio.android.domain.usecase

import com.picpay.desafio.android.domain.common.Logger
import com.picpay.desafio.android.domain.common.Resource
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository,
    private val logger: Logger
) {
    operator fun invoke(): Flow<Resource<List<User>>> = repository.getUsers().onStart {
        logger.d(TAG, "Buscando lista de usuários...")
    }

    companion object {
        private const val TAG = "GetUsersUseCase"
    }
}