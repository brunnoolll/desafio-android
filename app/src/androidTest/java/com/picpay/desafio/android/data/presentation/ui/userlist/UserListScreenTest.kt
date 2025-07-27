package com.picpay.desafio.android.data.presentation.ui.userlist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.presentation.theme.DesafioAndroidTheme
import com.picpay.desafio.android.presentation.ui.userlist.UserListScreen
import com.picpay.desafio.android.presentation.ui.userlist.UserListState
import org.junit.Rule
import org.junit.Test

class UserListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenStateIsSuccess_displaysUserList() {
        val fakeUsers = listOf(User(id = 1, name = "Eduardo Santos", username = "eduardo.santos", img =  ""))
        val successState = UserListState(users = fakeUsers)

        composeTestRule.setContent {
            DesafioAndroidTheme {
                UserListScreen(state = successState)
            }
        }

        composeTestRule.onNodeWithText("Contatos").assertIsDisplayed()

        composeTestRule.onNodeWithTag("user_list").assertExists()

        composeTestRule.onRoot().printToLog("SUCCESS_UI_TREE")

        composeTestRule.onNodeWithText("@eduardo.santos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Eduardo Santos").assertIsDisplayed()
    }

    @Test
    fun whenStateIsError_displaysErrorMessage() {
        val errorMessage = "Falha na conexão com o servidor"
        val errorState = UserListState(
            isLoading = false,
            users = emptyList(),
            error = errorMessage
        )

        composeTestRule.setContent {
            DesafioAndroidTheme {
                UserListScreen(state = errorState)
            }
        }

        composeTestRule.onNodeWithText("Contatos").assertIsDisplayed()

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()

        composeTestRule.onNodeWithTag("loading_indicator").assertDoesNotExist()
        composeTestRule.onNodeWithTag("user_list").assertDoesNotExist()
    }

    @Test
    fun whenStateIsLoading_displaysLoadingIndicator() {
        // Arrange: Crie um estado onde 'isLoading' é true e a lista está vazia.
        val loadingState = UserListState(isLoading = true, users = emptyList())

        // Act: Renderize a tela com o estado de loading.
        composeTestRule.setContent {
            DesafioAndroidTheme {
                UserListScreen(state = loadingState)
            }
        }

        // Assert: Verifique se os componentes corretos estão (ou não) na tela.
        composeTestRule.onNodeWithText("Contatos").assertIsDisplayed()

        // Verifica se o indicador de loading está sendo exibido
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()

        // Verifica se a lista de usuários NÃO existe
        composeTestRule.onNodeWithTag("user_list").assertDoesNotExist()
    }

}