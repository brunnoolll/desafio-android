package com.picpay.desafio.android.presentation.ui.userlist

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
        val errorState = UserListState(
            isLoading = false,
            users = emptyList(),
            isError = true
        )

        composeTestRule.setContent {
            DesafioAndroidTheme {
                UserListScreen(state = errorState)
            }
        }

        composeTestRule.onNodeWithText("Contatos").assertIsDisplayed()

        val expectedErrorMessage = "Ocorreu um erro. Tente novamente."
        composeTestRule.onNodeWithText(expectedErrorMessage).assertIsDisplayed()

        composeTestRule.onNodeWithTag("loading_indicator").assertDoesNotExist()
        composeTestRule.onNodeWithTag("user_list").assertDoesNotExist()
    }

    @Test
    fun whenStateIsLoading_displaysLoadingIndicator() {
        val loadingState = UserListState(isLoading = true, users = emptyList())

        composeTestRule.setContent {
            DesafioAndroidTheme {
                UserListScreen(state = loadingState)
            }
        }

        composeTestRule.onNodeWithText("Contatos").assertIsDisplayed()

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()

        composeTestRule.onNodeWithTag("user_list").assertDoesNotExist()
    }

}