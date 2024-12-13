package com.alaturing.incidentify.di

import com.alaturing.incidentify.login.data.UserRepository
import com.alaturing.incidentify.login.data.UserRepositoryMock
import com.alaturing.incidentify.remote.RemoteDatasource
import com.alaturing.incidentify.remote.RemoteDatasourceMock
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindMockUserRepository(r:UserRepositoryMock):UserRepository

    @Binds
    @Singleton
    abstract fun bindMockRemoteDatasource(ds:RemoteDatasourceMock):RemoteDatasource
}