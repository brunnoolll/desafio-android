package com.picpay.desafio.android.presentation.ui.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.common.Logger
import com.picpay.desafio.android.domain.common.Resource
import com.picpay.desafio.android.domain.usecase.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val logger: Logger
) : ViewModel() {

    private val _state = MutableStateFlow(UserListState())
    val state: StateFlow<UserListState> = _state.asStateFlow()

    init {
        logger.d(TAG, "UserListViewModel foi criado (init)")
        loadUsers()
    }

    private fun loadUsers() {
        getUsersUseCase.invoke().onEach { resource ->
            when (resource) {
                is Resource.Loading -> {
                    logger.d(TAG, "ViewModel recebeu: Resource.Loading")
                    _state.update {
                        it.copy(isLoading = true)
                    }
                }

                is Resource.Success -> {
                    logger.d(
                        TAG,
                        "ViewModel recebeu: Resource.Success com ${resource.data.size} usuários"
                    )
                    _state.update {
                        it.copy(
                            isLoading = false,
                            users = resource.data,
                            isError = false
                        )
                    }

                }

                is Resource.Error -> {
                    logger.d(
                        TAG,
                        "ViewModel recebeu: Resource.Error com msg: ${resource.error.message}"
                    )
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isError = true
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    companion object {
        private const val TAG = "UserListViewModel"
    }
}