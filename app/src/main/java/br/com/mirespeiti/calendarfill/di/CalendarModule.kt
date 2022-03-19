package br.com.mirespeiti.calendarfill.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import api.NyTime
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named


@InstallIn(SingletonComponent::class)
@Module
class CalendarModule {

    @Provides
    fun provideNYTimeApiService(retrofit: Retrofit): NyTime {
        return retrofit.create(NyTime::class.java)
    }

    @Provides
    @Named("BASE_URL")
    fun provideBaseUrl(): String {
        return "https://api.nytimes.com/svc/"
    }

    @Provides
    fun provideGsonBuilder(): GsonBuilder {
        return GsonBuilder()
    }

    @Provides
    fun provideConverterFactory(builder: GsonBuilder): Converter.Factory {
        return GsonConverterFactory.create(builder.create())
    }

    @Provides
    @Named("AuthInterceptor")
    fun provideOkHttpKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original: Request = chain.request()
            val htmlUrl: HttpUrl = original.url()

            val url: HttpUrl = htmlUrl.newBuilder()
                .addQueryParameter("api-key", "ORULCGmPGbmGAIg6VWPIGvB1pdna0A5P")
                .build()

            val requestBuild: Request.Builder = original.newBuilder()
                .url(url)

            val request: Request = requestBuild.build()
            chain.proceed(request)
        }
    }

    @Provides
    fun provideOkHttpAuth(
        @Named ("AuthInterceptor") authInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    fun provideRetrofit(
        @Named("BASE_URL") baseUrl: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

}
