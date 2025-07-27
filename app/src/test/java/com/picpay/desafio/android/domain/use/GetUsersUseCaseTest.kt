package com.picpay.desafio.android.domain.use

import com.picpay.desafio.android.domain.repository.UserRepository
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test


class GetUsersUseCaseTest {

    private val repository: UserRepository = mockk(relaxed = true)

    private val getUsersUseCase = GetUsersUseCase(repository)

    @Test
    fun `when use case is invoked, it should call getUsers from repository`() = runTest {
        getUsersUseCase()

        coVerify(exactly = 1) { repository.getUsers() }
    }
}