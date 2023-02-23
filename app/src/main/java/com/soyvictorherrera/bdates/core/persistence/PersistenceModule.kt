package com.soyvictorherrera.bdates.core.persistence

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PersistenceModule {

    @Binds
    abstract fun bindKeyValueStoreContract(keyValueStore: KeyValueStore): KeyValueStoreContract

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(application: Application): AppDatabase {
            return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                APP_DATABASE_NAME
            ).build()
        }

        @Provides
        @Singleton
        fun provideSharedPreferences(application: Application): SharedPreferences {
            return application.getSharedPreferences(
                APP_SHARED_PREFS_FILE_NAME,
                Context.MODE_PRIVATE
            )
        }
    }
}