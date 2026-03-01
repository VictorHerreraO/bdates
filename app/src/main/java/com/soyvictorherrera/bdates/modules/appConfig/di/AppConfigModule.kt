package com.soyvictorherrera.bdates.modules.appConfig.di

import com.soyvictorherrera.bdates.modules.appConfig.AppConfig
import com.soyvictorherrera.bdates.modules.appConfig.AppConfigContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppConfigModule {

    @Binds
    @Singleton
    abstract fun bindAppConfigContract(
        appConfig: AppConfig,
    ): AppConfigContract

}
