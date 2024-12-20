package com.alaturing.incidentify.di

import com.alaturing.incidentify.authentication.data.repository.UserRepository
import com.alaturing.incidentify.authentication.data.repository.UserRepositoryDefault
import com.alaturing.incidentify.authentication.data.local.UserLocalDatasource
import com.alaturing.incidentify.authentication.data.local.UserLocalDatasourceDS
import com.alaturing.incidentify.authentication.data.local.UserLocalDatasourceMock
import com.alaturing.incidentify.main.incident.data.repository.IncidentRepository
import com.alaturing.incidentify.main.incident.data.repository.IncidentRepositoryDefault
import com.alaturing.incidentify.authentication.data.remote.UserRemoteDatasource
import com.alaturing.incidentify.authentication.data.remote.UserRemoteDatasourceStrapi
import com.alaturing.incidentify.main.incident.data.remote.IncidentRemoteDatasource
import com.alaturing.incidentify.main.incident.data.remote.IncidentRemoteDatasourceStrapi
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
    abstract fun bindUserRepository(r: UserRepositoryDefault): UserRepository

    @Binds
    @Singleton
    abstract fun bindIncidentRepository(r:IncidentRepositoryDefault):IncidentRepository

    /**
     * Provee la fuente de datos remota para usuarios
     */
    @Binds
    @Singleton
    //abstract fun bindMockRemoteDatasource(ds:RemoteDatasourceMock):RemoteDatasource
    abstract fun bindUserRemoteDatasource(ds: UserRemoteDatasourceStrapi): UserRemoteDatasource

    @Binds
    @Singleton
    abstract fun bindIncidentRemoteDatasource(ds:IncidentRemoteDatasourceStrapi): IncidentRemoteDatasource

    /**
     * Provee la fuente de datos local para usuarios
     */
    @Binds
    @Singleton
    //abstract fun bindMockUserDatasourceLocal(ds:UserLocalDatasourceMock): UserLocalDatasource
    abstract fun bindUserDatasourceLocal(ds: UserLocalDatasourceDS): UserLocalDatasource


}