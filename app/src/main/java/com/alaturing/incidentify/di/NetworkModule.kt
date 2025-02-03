package com.alaturing.incidentify.di

import com.alaturing.incidentify.authentication.data.local.UserLocalDatasource
import com.alaturing.incidentify.authentication.data.remote.AuthenticationInterceptor
import com.alaturing.incidentify.common.remote.StrapiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


/**
 * [Module] to satisfy networking dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        const val STRAPI = "https://sterling-impala-mistakenly.ngrok-free.app"
    }

    /**
     * Anotación para calificar el interceptor de autenticación
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptorOkHttpClient

    /**
     * @param userLocalDS Datos locales persistidos del usuario
     * @return [Interceptor]
     */
    @Provides
    @AuthInterceptorOkHttpClient
    fun provideAuthenticationInterceptor(userLocalDS: UserLocalDatasource): Interceptor {
        return AuthenticationInterceptor(userLocalDS)
    }

    /**
     * Provides a customized [OkHttpClient]
     * @param interceptor Authentication interceptor
     * @return [OkHttpClient] Customized HTTP Client
     */
    @Provides
    @Singleton
    fun provideHttpClient(@AuthInterceptorOkHttpClient interceptor: Interceptor):OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient()
            .newBuilder()
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }
    @Provides
    @Singleton
    fun provideRemoteApi(client:OkHttpClient): StrapiApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(STRAPI)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return  retrofit.create(StrapiApi::class.java)
    }
}