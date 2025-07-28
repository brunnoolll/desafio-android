package com.picpay.desafio.android.data.repository

import com.google.common.truth.Truth.assertThat
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.local.model.UserEntity
import com.picpay.desafio.android.data.remote.ApiService
import com.picpay.desafio.android.data.remote.dto.UserDto
import com.picpay.desafio.android.domain.common.Logger
import com.picpay.desafio.android.domain.common.Resource
import com.picpay.desafio.android.presentation.userlist.TestLogger
import com.picpay.desafio.android.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class UserRepositoryImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: UserRepositoryImpl
    private val apiService: ApiService = mockk()
    private val userDao: UserDao = mockk()
    private val fakeLogger: Logger = TestLogger()

    @Before
    fun setUp() {
        repository = UserRepositoryImpl(apiService, userDao, fakeLogger)
    }

    @Test
    fun `getUsers when api succeeds should save data to dao and emit success`() = runTest {

        val fakeApiUsers = listOf(UserDto( id = "1", name =  "Api User", username = "api.user", imageUrl = ""))
        val fakeDbUsers = listOf(UserEntity(id = 1, name = "Api User", username = "api.user", img =  ""))


        coEvery { apiService.getUsers() } returns fakeApiUsers

        coEvery { userDao.deleteAll() } returns Unit
        coEvery { userDao.insertAll(any()) } returns Unit

        coEvery { userDao.getAllUsers() } returns flowOf(fakeDbUsers)

        repository.getUsers().test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val successResult = awaitItem()
            assertThat(successResult).isInstanceOf(Resource.Success::class.java)

            val users = (successResult as Resource.Success).data
            assertThat(users.size).isEqualTo(1)
            assertThat(users.first().name).isEqualTo("Api User")

            awaitComplete()
        }

        coVerify(exactly = 1) { apiService.getUsers() }
        coVerify(exactly = 1) { userDao.deleteAll() }
        coVerify(exactly = 1) { userDao.insertAll(fakeDbUsers) }
    }

    @Test
    fun `getUsers should always emit loading first`() = runTest {
        coEvery { apiService.getUsers() } throws IOException("Erro de rede simulado")
        coEvery { userDao.getAllUsers() } returns flowOf(emptyList())

        repository.getUsers().test {
            val firstEmission = awaitItem()

            assertThat(firstEmission).isInstanceOf(Resource.Loading::class.java)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getUsers when api fails and cache is not empty should emit success with cached data`() = runTest {

        val networkError = IOException("Falha na rede")

        coEvery { apiService.getUsers() } throws networkError

        val fakeCachedUsers = listOf(UserEntity( id = 1, name =  "Usuário do Cache", username = "cache.user", img =  "url"))
        coEvery { userDao.getAllUsers() } returns flowOf(fakeCachedUsers)


        repository.getUsers().test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)

            val users = (result as Resource.Success).data
            assertThat(users.first().name).isEqualTo("Usuário do Cache")

            awaitComplete()
        }

        coVerify(exactly = 1) { apiService.getUsers() }

        coVerify(exactly = 0) { userDao.deleteAll() }
        coVerify(exactly = 0) { userDao.insertAll(any()) }
    }
}