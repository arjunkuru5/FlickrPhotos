package com.example.flickrphotos.di

import com.example.flickrphotos.domain.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
class AppModule {

    @ActivityRetainedScoped
    @Provides
    fun provideUserHomeService(retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }
}