package com.alaturing.incidentify.di

import com.alaturing.incidentify.authentication.data.local.UserLocalDatasource
import com.alaturing.incidentify.remote.AuthenticationInterceptor
import com.alaturing.incidentify.remote.StrapiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton
private const val STRAPI = "https://sterling-impala-mistakenly.ngrok-free.app/api/"

/**
 * Modulo de Hilt para la red
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

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
    fun provideAutenticationInterceptor(userLocalDS: UserLocalDatasource): Interceptor {
        return AuthenticationInterceptor(userLocalDS)
    }

    /**
     * @param interceptor
     * @return [OkHttpClient] cliente de OkHttp con el interceptor añadido
     */
    @Provides
    @Singleton
    fun provideHttpClient(@AuthInterceptorOkHttpClient interceptor: Interceptor):OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(interceptor)
            .build()
    }
    @Provides
    @Singleton
    fun provideRemoteApi(client:OkHttpClient):StrapiApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(STRAPI)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return  retrofit.create(StrapiApi::class.java)
    }
}