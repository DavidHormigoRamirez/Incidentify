package com.alaturing.incidentify.di

import com.alaturing.incidentify.login.data.IUserRepository
import com.alaturing.incidentify.login.data.UserRepositoryMock
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
    abstract fun bindMockUserRepository(r:UserRepositoryMock):IUserRepository
}