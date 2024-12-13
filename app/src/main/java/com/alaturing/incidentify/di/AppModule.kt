package com.alaturing.incidentify.di

import com.alaturing.incidentify.authentication.data.repository.UserRepository
import com.alaturing.incidentify.authentication.data.repository.UserRepositoryDefault
import com.alaturing.incidentify.authentication.data.local.UserLocalDatasource
import com.alaturing.incidentify.authentication.data.local.UserLocalDatasourceMock
import com.alaturing.incidentify.remote.RemoteDatasource
import com.alaturing.incidentify.remote.RemoteDatasourceStrapi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para satisfacer dependecias
 * Lo hace a nivel de SingletonComponent para que tenga validez
 * durante toda la aplicación
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    /**
     * Provee el repositorio de usuarios
     */
    @Binds
    @Singleton
    abstract fun bindMockUserRepository(r: UserRepositoryDefault): UserRepository

    /**
     * Provee la fuente de datos remota
     */
    @Binds
    @Singleton
    //abstract fun bindMockRemoteDatasource(ds:RemoteDatasourceMock):RemoteDatasource
    abstract fun bindStrapiRemoteDatasource(ds: RemoteDatasourceStrapi):RemoteDatasource

    /**
     * Provee la fuente de datos local
     */
    @Binds
    @Singleton
    abstract fun bindMockUserDatasourceLocal(ds:UserLocalDatasourceMock): UserLocalDatasource
}