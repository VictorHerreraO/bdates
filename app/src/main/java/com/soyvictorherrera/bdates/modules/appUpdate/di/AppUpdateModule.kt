package com.soyvictorherrera.bdates.modules.appUpdate.di

import com.soyvictorherrera.bdates.modules.appUpdate.data.preferences.UpdatePreferences
import com.soyvictorherrera.bdates.modules.appUpdate.data.preferences.UpdatePreferencesContract
import com.soyvictorherrera.bdates.modules.appUpdate.domain.MigrateAppFromVersionCodeUseCase
import com.soyvictorherrera.bdates.modules.appUpdate.domain.MigrateAppFromVersionCodeUseCaseContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppUpdateModule {

    @Binds
    abstract fun bindUpdatePreferencesContract(
        updatePreferences: UpdatePreferences,
    ): UpdatePreferencesContract

    @Binds
    abstract fun bindAppMigrationsContract(
        appMigrations: MigrateAppFromVersionCodeUseCase,
    ): MigrateAppFromVersionCodeUseCaseContract

}
