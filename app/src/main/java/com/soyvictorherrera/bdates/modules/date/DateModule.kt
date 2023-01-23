package com.soyvictorherrera.bdates.modules.date

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DateModule {

    @Binds
    abstract fun bindDateProviderContract(dateProvider: DateProvider): DateProviderContract
}