package com.soyvictorherrera.bdates.core.resource

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ResourceModule {

    @Provides
    fun provideResourceManagerContract(application: Application): ResourceManagerContract {
        return ResourceManager(application)
    }

}
