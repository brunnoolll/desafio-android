package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.local.model.toDomain
import com.picpay.desafio.android.data.remote.ApiService
import com.picpay.desafio.android.data.remote.dto.toEntity
import com.picpay.desafio.android.domain.common.Logger
import com.picpay.desafio.android.domain.exception.ApiException
import com.picpay.desafio.android.domain.common.Resource
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val logger: Logger
) : UserRepository {

    override fun getUsers(): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading)
        logger.d(TAG, "Iniciando busca de usuários...")

        try {
            logger.d(TAG, "Buscando usuários da API...")
            val userDtos = apiService.getUsers()
            val userEntities = userDtos.map { it.toEntity() }

            logger.d(TAG, "${userDtos.size} usuários recebidos da API. Atualizando o cache...")
            userDao.deleteAll()
            userDao.insertAll(userEntities)

            val freshUsers = userDao.getAllUsers().first().map { it.toDomain() }
            emit(Resource.Success(freshUsers))
            logger.d(TAG, "Cache atualizado e dados emitidos com sucesso.")

        } catch (e: Exception) {
            logger.e(TAG, "Falha ao buscar dados da API.", e)
            val cachedUsers = userDao.getAllUsers().first().map { it.toDomain() }

            if (cachedUsers.isNotEmpty()) {
                logger.d(TAG, "API falhou, mas há dados no cache. Emitindo dados do cache.")
                emit(Resource.Success(cachedUsers))
            } else {
                logger.e(TAG, "API falhou e o cache está vazio. Emitindo erro.")
                val apiException = when (e) {
                    is HttpException -> when (e.code()) {
                        401 -> ApiException.UnauthorizedError()
                        in 500..599 -> ApiException.ServerError()
                        else -> ApiException.UnknownError("Erro HTTP: ${e.code()}")
                    }
                    is IOException -> ApiException.NetworkError()
                    else -> ApiException.UnknownError(e.message ?: "Erro desconhecido")
                }
                emit(Resource.Error(apiException))
            }
        }
    }

    companion object {
        private const val TAG = "UserRepository"
    }

}