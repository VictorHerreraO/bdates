package com.soyvictorherrera.bdates.core.network

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

private const val OK_HTTP_TAG = "OkHttp"

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun bindAuthenticator(
        apiAuthenticator: ApiAuthenticator,
    ): Authenticator

    companion object {
        @Provides
        @IntoSet
        fun provideHttpLoggingInterceptor(

        ): Interceptor = HttpLoggingInterceptor { message ->
            Timber.tag(OK_HTTP_TAG).d(message)
        }.apply {
            level = Level.BODY
        }

        @Provides
        fun provideOkHttpClientBuilder(
            interceptors: Set<@JvmSuppressWildcards Interceptor>,
        ): OkHttpClient.Builder {
            var builder = OkHttpClient.Builder()
            interceptors.forEach { interceptor ->
                builder = builder.addInterceptor(interceptor)
            }
            return builder
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(
            builder: OkHttpClient.Builder,
        ): OkHttpClient = builder.build()

        @Provides
        @Singleton
        @Authenticated
        fun provideAuthenticatedOkHttpClient(
            builder: OkHttpClient.Builder,
            authenticator: Authenticator,
        ): OkHttpClient = builder.authenticator(authenticator).build()

        @Provides
        @IntoSet
        fun provideMoshiConverterFactory(

        ): Converter.Factory = MoshiConverterFactory.create()

        @Provides
        fun retrofitBuilder(
            converterFactories: Set<@JvmSuppressWildcards Converter.Factory>,
        ): Retrofit.Builder {
            var builder = Retrofit.Builder()
                .baseUrl(Endpoints.BASE_URL)

            converterFactories.forEach { factory ->
                builder = builder.addConverterFactory(factory)
            }

            return builder

        }

        @Provides
        fun provideRetrofit(
            okHttpClient: OkHttpClient,
            builder: Retrofit.Builder,
        ): Retrofit {
            return builder.client(okHttpClient).build()
        }

        @Provides
        @Authenticated
        fun provideAuthenticatedRetrofit(
            @Authenticated okHttpClient: OkHttpClient,
            builder: Retrofit.Builder,
        ): Retrofit {
            return builder.client(okHttpClient).build()
        }
    }
}
