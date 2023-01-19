package com.soyvictorherrera.bdates.modules.notifications

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationsModule {


    @Binds
    @Singleton
    abstract fun bindContext(
        application: Application
    ): Context

    @Binds
    @Singleton
    abstract fun bindNotificationManagerContract(
        notificationManager: NotificationManager
    ): NotificationManagerContract
}