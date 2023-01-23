package com.soyvictorherrera.bdates.modules.eventList.di

import android.app.Application
import android.content.res.AssetManager
import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.AssetFileManager
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.AssetFileManagerContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDatasource
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
    abstract fun bindEventDataSourceContract(
        eventDatasource: EventDatasource
    ): EventDataSourceContract

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

    companion object {
        @Provides
        fun provideAssetManager(app: Application): AssetManager {
            return app.assets
        }

        @Provides
        fun bindJsonToEventMapper(): Mapper<JSONObject, Event> {
            return JsonToEventMapper
        }
    }

}
