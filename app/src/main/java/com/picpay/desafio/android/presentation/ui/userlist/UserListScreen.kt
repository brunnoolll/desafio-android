package com.picpay.desafio.android.presentation.ui.userlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.picpay.desafio.android.R

@Composable
fun UserListRoute(
    viewModel: UserListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    UserListScreen(state = state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    state: UserListState,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(color = Color.White, text = stringResource(R.string.title), style = MaterialTheme.typography.headlineMedium) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .background(color = Color.Black)
                .fillMaxSize()
                .padding(innerPadding),
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
                Text(text = stringResource(R.string.error))
            }
        }
    }
}