package com.picpay.desafio.android.di

import android.content.Context
import com.picpay.desafio.android.data.local.dao.UserDao
import com.picpay.desafio.android.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideDeviceDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
}