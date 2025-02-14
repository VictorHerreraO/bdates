package com.soyvictorherrera.bdates.modules.appinfo.di

import com.soyvictorherrera.bdates.BuildConfig
import com.soyvictorherrera.bdates.modules.appinfo.domain.AppInfoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppInfoModule {

    @Provides
    @Singleton
    fun provideAppInfoProvider(): AppInfoProvider {
        return object: AppInfoProvider {
            override val isDebugBuild: Boolean
                get() = BuildConfig.DEBUG
        }
    }
}