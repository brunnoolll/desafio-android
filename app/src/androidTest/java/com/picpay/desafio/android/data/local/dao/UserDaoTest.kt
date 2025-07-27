package com.picpay.desafio.android.data.local.dao


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.picpay.desafio.android.data.local.database.AppDatabase
import com.picpay.desafio.android.data.local.model.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertUserAndGetAllUsers_returnsInsertedUser() = runTest {
        val userEntity = UserEntity( id = 1, name =  "Name", username =  "username", img = "url")
        val userList = listOf(userEntity)

        userDao.insertAll(userList)
        val usersFromDb = userDao.getAllUsers().first()

        assertThat(usersFromDb).isEqualTo(userList)
        assertThat(usersFromDb.first().name).isEqualTo("Name")
    }

    @Test
    fun deleteAllUsers_clearsTheDatabase() = runTest {
        val userEntity = UserEntity( id = 1, name =  "Name", username =  "username", img = "url")
        userDao.insertAll(listOf(userEntity))

        userDao.deleteAll()
        val usersFromDb = userDao.getAllUsers().first()

        assertThat(usersFromDb).isEmpty()
    }

    @Test
    fun insertUserWithSameId_replacesExistingUser() = runTest {

        val originalUser = UserEntity(id = 1, name =  "Bruno Original", username =  "bruno.original", img = "url")
        userDao.insertAll(listOf(originalUser))

        val updatedUser = UserEntity(id = 1, name = "Bruno Atualizado", username =  "bruno.atualizado", img =  "url_nova")

        userDao.insertAll(listOf(updatedUser))
        val usersFromDb = userDao.getAllUsers().first()

        assertThat(usersFromDb.size).isEqualTo(1)

        assertThat(usersFromDb.first()).isEqualTo(updatedUser)
        assertThat(usersFromDb.first().name).isEqualTo("Bruno Atualizado")
    }
}