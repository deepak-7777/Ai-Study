package com.aistudyos.app.di

import com.aistudyos.app.BuildConfig
import com.aistudyos.app.data.remote.api.*
import com.aistudyos.app.data.remote.interceptors.AuthInterceptor
import com.aistudyos.app.data.remote.interceptors.NetworkMonitorInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
        }

    @Provides @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        networkMonitor: NetworkMonitorInterceptor,
        logging: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(networkMonitor)
        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides @Singleton fun provideAuthApi(r: Retrofit): AuthApiService = r.create(AuthApiService::class.java)
    @Provides @Singleton fun provideUserApi(r: Retrofit): UserApiService = r.create(UserApiService::class.java)
    @Provides @Singleton fun provideSubjectApi(r: Retrofit): SubjectApiService = r.create(SubjectApiService::class.java)
    @Provides @Singleton fun provideMaterialApi(r: Retrofit): MaterialApiService = r.create(MaterialApiService::class.java)
    @Provides @Singleton fun provideChatApi(r: Retrofit): ChatApiService = r.create(ChatApiService::class.java)
    @Provides @Singleton fun provideNotesApi(r: Retrofit): NotesApiService = r.create(NotesApiService::class.java)
    @Provides @Singleton fun provideQuizApi(r: Retrofit): QuizApiService = r.create(QuizApiService::class.java)
    @Provides @Singleton fun provideRevisionApi(r: Retrofit): RevisionApiService = r.create(RevisionApiService::class.java)
    @Provides @Singleton fun provideAnalyticsApi(r: Retrofit): AnalyticsApiService = r.create(AnalyticsApiService::class.java)
}
