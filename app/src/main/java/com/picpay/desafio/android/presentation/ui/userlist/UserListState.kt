package com.picpay.desafio.android.presentation.ui.userlist

import com.picpay.desafio.android.domain.model.User

data class UserListState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null
)