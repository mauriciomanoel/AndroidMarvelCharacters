package com.mauricio.marvel.di.module

import com.mauricio.marvel.characters.repositories.CharacterRepository
import com.mauricio.marvel.network.RetrofitApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideCharacterRepository(apiService: RetrofitApiService) = CharacterRepository(apiService)
}