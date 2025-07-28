package com.picpay.desafio.android.data.local.database


import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class AppDatabaseTest {

    @Test
    fun getDatabase_returnsNonNullDao() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        val db = AppDatabase.getDatabase(context)
        val dao = db.userDao()

        assertThat(dao).isNotNull()
    }

    @Test
    fun getDatabase_returnsSameInstance() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()

        val dbInstance1 = AppDatabase.getDatabase(context)
        val dbInstance2 = AppDatabase.getDatabase(context)

        assertThat(dbInstance1).isSameInstanceAs(dbInstance2)
    }
}