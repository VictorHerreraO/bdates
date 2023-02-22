package com.soyvictorherrera.bdates.modules.circles.di

import com.soyvictorherrera.bdates.core.arch.Mapper
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.CircleEntity
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.LocalCircleDataSource
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.LocalCircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.data.mapper.CircleEntityToModelMapper
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepository
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepositoryContract
import com.soyvictorherrera.bdates.modules.circles.domain.model.Circle
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CirclesModule {

    @Binds
    abstract fun bindLocalCircleDataSourceContract(
        dataSource: LocalCircleDataSource
    ): LocalCircleDataSourceContract

    @Binds
    abstract fun bind(
        repository: CircleRepository
    ): CircleRepositoryContract

    companion object {
        @Provides
        fun provideCircleEntityToModelMapper(): Mapper<CircleEntity, Circle> {
            return CircleEntityToModelMapper
        }
    }
}