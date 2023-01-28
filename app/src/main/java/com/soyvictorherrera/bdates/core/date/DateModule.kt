package com.soyvictorherrera.bdates.core.date

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DateModule {

    @Binds
    abstract fun bindDateProviderContract(dateProvider: DateProvider): DateProviderContract

    @Binds
    abstract fun bindDateFormattersContract(dateFormatters: DateFormatters): DateFormattersContract

    companion object {
        @Provides
        @Singleton
        fun provideClock(): Clock {
            return Clock.systemDefaultZone()
        }
    }
}