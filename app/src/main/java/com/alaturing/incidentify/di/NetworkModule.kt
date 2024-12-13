package com.alaturing.incidentify.di

import com.alaturing.incidentify.remote.StrapiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val STRAPI = "https://sterling-impala-mistakenly.ngrok-free.app"
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRemoteApi():StrapiApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(STRAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return  retrofit.create(StrapiApi::class.java)
    }
}