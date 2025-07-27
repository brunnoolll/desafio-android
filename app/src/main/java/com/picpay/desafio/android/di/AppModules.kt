package com.picpay.desafio.android.di

import com.picpay.desafio.android.data.logger.AndroidLogger
import com.picpay.desafio.android.domain.common.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindLogger(
        androidLogger: AndroidLogger
    ): Logger
}