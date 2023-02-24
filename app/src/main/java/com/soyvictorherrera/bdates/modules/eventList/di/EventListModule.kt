package com.soyvictorherrera.bdates.modules.eventList.di

import android.app.Application
import android.content.res.AssetManager
import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.core.persistence.AppDatabase
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetEventDatasource
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetEventDatasourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetFileManager
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.assets.AssetFileManagerContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.EventDao
import com.soyvictorherrera.bdates.modules.eventList.data.mapper.JsonToEventMapper
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepository
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListUseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetDayEventListUseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetDayEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetEventListUseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetNonDayEventListUseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetNonDayEventListUseCaseContract
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetUpcomingEventListUseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetUpcomingEventListUseCaseContract
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.json.JSONObject

@Module
@InstallIn(SingletonComponent::class)
abstract class EventListModule {

    @Binds
    abstract fun bindGetEventListUseCase(
        getEventListUseCase: GetEventListUseCase
    ): GetEventListUseCaseContract

    @Binds
    abstract fun bindFilterEventListUseCase(
        filterEventListUseCase: FilterEventListUseCase
    ): FilterEventListUseCaseContract

    @Binds
    abstract fun bindAssetFileManager(
        assetFileManager: AssetFileManager
    ): AssetFileManagerContract

    @Binds
    abstract fun bindAssetEventDatasourceContract(
        assetEventDatasource: AssetEventDatasource
    ): AssetEventDatasourceContract

    @Binds
    abstract fun bindEventRepositoryContract(
        eventRepository: EventRepository
    ): EventRepositoryContract

    @Binds
    abstract fun bindGetDayEventListUseCaseContract(
        getDayEventListUseCase: GetDayEventListUseCase
    ): GetDayEventListUseCaseContract

    @Binds
    abstract fun bindGetNonDayEventListUseCaseContract(
        getNonDayEventListUseCase: GetNonDayEventListUseCase
    ): GetNonDayEventListUseCaseContract

    @Binds
    abstract fun bindGetUpcomingEventListUseCaseContract(
        getUpcomingEventListUseCase: GetUpcomingEventListUseCase
    ): GetUpcomingEventListUseCaseContract

    companion object {
        @Provides
        fun provideAssetManager(app: Application): AssetManager {
            return app.assets
        }

        @Provides
        fun bindJsonToEventMapper(): Mapper<JSONObject, Event> {
            return JsonToEventMapper
        }

        @Provides
        fun provideEventDao(appDatabase: AppDatabase): EventDao {
            return appDatabase.eventDao()
        }
    }

}
