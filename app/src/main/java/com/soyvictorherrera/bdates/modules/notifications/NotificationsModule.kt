package com.soyvictorherrera.bdates.modules.notifications

import android.app.Application
import android.content.Context
import com.soyvictorherrera.bdates.modules.notifications.data.preferences.NotificationPreferences
import com.soyvictorherrera.bdates.modules.notifications.data.preferences.NotificationPreferencesContract
import com.soyvictorherrera.bdates.modules.notifications.delegate.DayNotificationDelegate
import com.soyvictorherrera.bdates.modules.notifications.delegate.DayNotificationDelegateContract
import com.soyvictorherrera.bdates.modules.notifications.delegate.UpcomingEventNotificationDelegate
import com.soyvictorherrera.bdates.modules.notifications.delegate.UpcomingEventNotificationDelegateContract
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
        application: Application,
    ): Context

    @Binds
    @Singleton
    abstract fun bindNotificationManagerContract(
        notificationManager: NotificationManager,
    ): NotificationManagerContract

    @Binds
    @Singleton
    abstract fun bindDayNotificationDelegateContract(
        dayNotificationDelegate: DayNotificationDelegate,
    ): DayNotificationDelegateContract

    @Binds
    @Singleton
    abstract fun bindUpcomingEventNotificationDelegateContract(
        upcomingEventNotificationDelegate: UpcomingEventNotificationDelegate,
    ): UpcomingEventNotificationDelegateContract

    @Binds
    @Singleton
    abstract fun bindNotificationPreferencesContract(
        notificationPreferences: NotificationPreferences,
    ): NotificationPreferencesContract
}