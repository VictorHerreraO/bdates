package com.soyvictorherrera.bdates.modules.eventList.di

import com.soyvictorherrera.bdates.core.arch.UseCase
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDataSourceContract
import com.soyvictorherrera.bdates.modules.eventList.data.datasource.EventDatasource
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepository
import com.soyvictorherrera.bdates.modules.eventList.data.repository.EventRepositoryContract
import com.soyvictorherrera.bdates.modules.eventList.domain.model.Event
import com.soyvictorherrera.bdates.modules.eventList.domain.usecase.GetEventListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(SingletonComponent::class)
object EventListModule {

    @Provides
    fun provideEventDataSourceContract(): EventDataSourceContract {
        return EventDatasource()
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

}
