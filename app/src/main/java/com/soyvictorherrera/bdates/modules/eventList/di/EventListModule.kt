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
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.LocalEventDataSource
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.local.LocalEventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.remote.EventApi
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.remote.RemoteEventDataSource
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.remote.RemoteEventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.mapper.EventDtoToModelMapper
import com.soyvictorherrera.bdates.modules.eventList.data.mapper.EventDtoToModelMapperContract
import com.soyvictorherrera.bdates.modules.eventList.data.mapper.EventEntityToModelMapper
import com.soyvictorherrera.bdates.modules.eventList.data.mapper.EventEntityToModelMapperContract
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
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.UpdateEventsUseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.UpdateEventsUseCaseContract
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.json.JSONObject
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class EventListModule {
    //region Data
    @Binds
    abstract fun bindAssetFileManager(
        assetFileManager: AssetFileManager,
    ): AssetFileManagerContract

    @Binds
    abstract fun bindAssetEventDatasourceContract(
        assetEventDatasource: AssetEventDatasource,
    ): AssetEventDatasourceContract

    @Binds
    @Singleton
    abstract fun bindEventRepositoryContract(
        eventRepository: EventRepository,
    ): EventRepositoryContract

    @Binds
    abstract fun bindLocalEventDataSourceContract(
        localEventDataSource: LocalEventDataSource,
    ): LocalEventDataSourceContract

    @Binds
    abstract fun bindRemoteEventDataSourceContract(
        remoteEventDatasource: RemoteEventDataSource,
    ): RemoteEventDataSourceContract
    //endregion

    //region Use cases
    @Binds
    abstract fun bindGetDayEventListUseCaseContract(
        getDayEventListUseCase: GetDayEventListUseCase,
    ): GetDayEventListUseCaseContract

    @Binds
    abstract fun bindGetNonDayEventListUseCaseContract(
        getNonDayEventListUseCase: GetNonDayEventListUseCase,
    ): GetNonDayEventListUseCaseContract

    @Binds
    abstract fun bindGetUpcomingEventListUseCaseContract(
        getUpcomingEventListUseCase: GetUpcomingEventListUseCase,
    ): GetUpcomingEventListUseCaseContract

    @Binds
    abstract fun bindUpdateEventsUseCaseContract(
        updateEventsUseCase: UpdateEventsUseCase,
    ): UpdateEventsUseCaseContract

    @Binds
    abstract fun bindGetEventListUseCase(
        getEventListUseCase: GetEventListUseCase,
    ): GetEventListUseCaseContract

    @Binds
    abstract fun bindFilterEventListUseCase(
        filterEventListUseCase: FilterEventListUseCase,
    ): FilterEventListUseCaseContract
    //endregion

    companion object {
        @Provides
        fun provideAssetManager(app: Application): AssetManager {
            return app.assets
        }

        @Provides
        fun provideEventDao(appDatabase: AppDatabase): EventDao {
            return appDatabase.eventDao()
        }

        //region Mappers
        @Provides
        fun provideJsonToEventMapper(): Mapper<JSONObject, Event> {
            return JsonToEventMapper
        }

        @Provides
        fun provideEventEntityToModelMapperContract(): EventEntityToModelMapperContract {
            return EventEntityToModelMapper
        }

        @Provides
        fun provideEventDtoToModelMapperContract(): EventDtoToModelMapperContract {
            return EventDtoToModelMapper
        }
        //endregion

        @Provides
        fun provideEventApi(retrofit: Retrofit): EventApi {
            return retrofit.create(EventApi::class.java)
        }
    }

}
