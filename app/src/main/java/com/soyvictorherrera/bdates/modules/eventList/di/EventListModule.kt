package com.soyvictorherrera.bdates.modules.eventList.di

import android.app.Application
import android.content.res.AssetManager
import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.AssetFileManager
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.AssetFileManagerContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDatasource
import com.soyvictorherrera.bdates.modules.eventList.data.mapper.JsonToEventMapper
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepository
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListArgs
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.FilterEventListUseCase
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetEventListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

@Module
@InstallIn(SingletonComponent::class)
object EventListModule {

    @Provides
    fun provideAssetManager(app: Application): AssetManager {
        return app.assets
    }

    @Provides
    fun provideAssetFileManager(assetManager: AssetManager): AssetFileManagerContract {
        return AssetFileManager(assetManager)
    }

    @Provides
    fun provideEventDataSourceContract(
        assetFileManager: AssetFileManagerContract,
        jsonToEventMapper: Mapper<JSONObject, Event>
    ): EventDataSourceContract {
        return EventDatasource(
            assets = assetFileManager,
            jsonToEventMapper = jsonToEventMapper
        )
    }

    @Provides
    fun provideEventRepositoryContract(
        dataSource: EventDataSourceContract
    ): EventRepositoryContract {
        return EventRepository(
            dataSource = dataSource
        )
    }

    @Provides
    fun provideGetEventListUseCase(
        repository: EventRepositoryContract
    ): UseCase<Unit, Flow<List<Event>>> {
        return GetEventListUseCase(
            repository = repository
        )
    }

    @Provides
    fun provideFilterEventListUseCase(): UseCase<FilterEventListArgs, Result<List<Event>>> {
        return FilterEventListUseCase()
    }

    @Provides
    fun provideJsonToEventMapper(): Mapper<JSONObject, Event> {
        return JsonToEventMapper
    }

}
