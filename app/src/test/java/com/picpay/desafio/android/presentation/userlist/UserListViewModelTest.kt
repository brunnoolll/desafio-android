package com.picpay.desafio.android.presentation.userlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.picpay.desafio.android.domain.common.Logger
import com.picpay.desafio.android.domain.common.Resource
import com.picpay.desafio.android.domain.exception.ApiException
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import com.picpay.desafio.android.presentation.ui.userlist.UserListViewModel
import com.picpay.desafio.android.util.MainCoroutineRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class TestLogger : Logger {
    override fun d(tag: String, message: String) {}
    override fun e(tag: String, message: String, throwable: Throwable?) { }
}

@ExperimentalCoroutinesApi
class UserListViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val getUsersUseCase: GetUsersUseCase = mockk()
    private val fakeLogger: Logger = TestLogger()

    private lateinit var viewModel: UserListViewModel

    @Test
    fun `when use case returns success, state should be updated with user list`() = runTest {
        val fakeUserList = listOf(User(id = 1, name = "Bruno", username = "bruno.rios", img =  ""))
        val successFlow = flowOf(Resource.Success(fakeUserList))
        every { getUsersUseCase() } returns successFlow

        viewModel = UserListViewModel(getUsersUseCase, fakeLogger)

        viewModel.state.test {
            val finalState = awaitItem()

            assertFalse("O loading deveria ter terminado", finalState.isLoading)
            assertEquals("A lista de usuários deveria ser a lista falsa", fakeUserList, finalState.users)
            assertFalse("Não deveria haver erro (isError deveria ser falso)", finalState.isError)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when use case returns error, state should be updated with error message`() = runTest {

        val fakeError = ApiException.NetworkError()
        val errorFlow = flowOf(Resource.Error(fakeError))
        every { getUsersUseCase() } returns errorFlow

        viewModel = UserListViewModel(getUsersUseCase, fakeLogger)

        viewModel.state.test {
            val errorState = awaitItem()

            assertFalse("O loading deveria ser falso após o erro", errorState.isLoading)
            assertTrue("A lista de usuários deveria estar vazia em caso de erro inicial", errorState.users.isEmpty())
            assertTrue("A flag de erro deveria ser verdadeira", errorState.isError)

            cancelAndIgnoreRemainingEvents()
        }
    }

}