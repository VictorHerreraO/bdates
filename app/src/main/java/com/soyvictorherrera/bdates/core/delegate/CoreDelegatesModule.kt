package com.soyvictorherrera.bdates.core.delegate

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreDelegatesModule {

    @Binds
    abstract fun bindApplicationCreatedCallbackDelegateContract(
        applicationCreatedCallbackDelegate: ApplicationCreatedCallbackDelegate
    ): ApplicationCreatedCallbackDelegateContract

}
