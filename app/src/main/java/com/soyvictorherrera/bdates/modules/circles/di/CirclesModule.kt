package com.soyvictorherrera.bdates.modules.circles.di

import com.soyvictorherrera.bdates.core.persistence.AppDatabase
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.CircleDao
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.LocalCircleDataSource
import com.soyvictorherrera.bdates.modules.circles.data.datasource.local.LocalCircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.data.datasource.remote.CircleApi
import com.soyvictorherrera.bdates.modules.circles.data.datasource.remote.RemoteCircleDataSource
import com.soyvictorherrera.bdates.modules.circles.data.datasource.remote.RemoteCircleDataSourceContract
import com.soyvictorherrera.bdates.modules.circles.data.mapper.CircleResponseResponseDtoToModelMapper
import com.soyvictorherrera.bdates.modules.circles.data.mapper.CircleResponseDtoToModelMapperContract
import com.soyvictorherrera.bdates.modules.circles.data.mapper.CircleEntityToModelMapper
import com.soyvictorherrera.bdates.modules.circles.data.mapper.CircleEntityToModelMapperContract
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferences
import com.soyvictorherrera.bdates.modules.circles.data.preferences.CirclePreferencesContract
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepository
import com.soyvictorherrera.bdates.modules.circles.data.repository.CircleRepositoryContract
import com.soyvictorherrera.bdates.modules.circles.domain.CreateLocalCircleUseCase
import com.soyvictorherrera.bdates.modules.circles.domain.CreateLocalCircleUseCaseContract
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class CirclesModule {

    //region Data
    @Binds
    abstract fun bindLocalCircleDataSourceContract(
        dataSource: LocalCircleDataSource,
    ): LocalCircleDataSourceContract

    @Binds
    abstract fun bindRemoteCircleDataSourceContract(
        dataSource: RemoteCircleDataSource,
    ): RemoteCircleDataSourceContract

    @Binds
    abstract fun bindCircleRepositoryContract(
        repository: CircleRepository,
    ): CircleRepositoryContract
    //endregion

    @Binds
    abstract fun bindCirclePreferencesContract(
        circlePreferences: CirclePreferences,
    ): CirclePreferencesContract

    //region Use cases
    @Binds
    abstract fun bindCreateLocalCircleUseCaseContract(
        createLocalCircleUseCase: CreateLocalCircleUseCase,
    ): CreateLocalCircleUseCaseContract
    //endregion

    companion object {
        @Provides
        fun provideCircleEntityToModelMapperContract(

        ): CircleEntityToModelMapperContract = CircleEntityToModelMapper

        @Provides
        fun provideCircleResponseDtoToModelMapperContract(

        ): CircleResponseDtoToModelMapperContract = CircleResponseResponseDtoToModelMapper

        @Provides
        fun provideCircleDao(appDatabase: AppDatabase): CircleDao {
            return appDatabase.circleDao()
        }

        @Provides
        fun provideCircleApi(
            retrofit: Retrofit,
        ): CircleApi = retrofit.create(CircleApi::class.java)
    }
}