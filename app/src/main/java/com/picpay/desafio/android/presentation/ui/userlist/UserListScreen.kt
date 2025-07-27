package com.picpay.desafio.android.presentation.ui.userlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun UserListRoute(
    viewModel: UserListViewModel = hiltViewModel()
) {
    // Coleta o estado do ViewModel
    val state by viewModel.state.collectAsState()
    // Passa o estado para a nossa tela "burra"
    UserListScreen(state = state)
}

@Composable
fun UserListScreen(
    state: UserListState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Contatos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.users.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("user_list")
                ) {
                    items(state.users) { user ->
                        UserListItem(
                            user = user,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            if (state.isLoading && state.users.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.testTag("loading_indicator"))
            }

            if (state.error != null && state.users.isEmpty()) {
                Text(text = state.error)
            }
        }
    }
}