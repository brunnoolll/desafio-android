package com.picpay.desafio.android.data.repository


import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.local.model.toDomain
import com.picpay.desafio.android.data.remote.ApiService
import com.picpay.desafio.android.data.remote.dto.toEntity
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
    private val userDao: UserDao
) : UserRepository {

    override fun getUsers(): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading)

        try {
            val userDtos = apiService.getUsers()
            val userEntities = userDtos.map { it.toEntity() }

            userDao.deleteAll()
            userDao.insertAll(userEntities)

            val freshUsers = userDao.getAllUsers().first().map { it.toDomain() }
            emit(Resource.Success(freshUsers))

        } catch (e: Exception) {
            val cachedUsers = userDao.getAllUsers().first().map { it.toDomain() }

            if (cachedUsers.isNotEmpty()) {
                emit(Resource.Success(cachedUsers))
            } else {
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
}