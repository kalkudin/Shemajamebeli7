package com.example.di

import com.example.data.home.RecyclerDataRepositoryImpl
import com.example.domain.home.RecyclerDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRecyclerDataRepository() : RecyclerDataRepository {
        return RecyclerDataRepositoryImpl()
    }
}