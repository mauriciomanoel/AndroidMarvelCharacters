package com.mauricio.marvel.characters.repositories

import android.util.Log
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.filter
import androidx.paging.liveData
import com.mauricio.marvel.characters.models.Character
import com.mauricio.marvel.network.RetrofitApiService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(private val apiService: RetrofitApiService) {

    fun getCharacterEvents(characterId:Long) =
        Pager(
            getDefaultPageConfig(),
            pagingSourceFactory = { CharacterEventsPagingSource(characterId, apiService) }
        ).liveData.map {
            val characterMap = mutableSetOf<Long>()
            it.filter { character ->
                if (characterMap.contains(character.id)) {
                    false
                } else {
                    characterMap.add(character.id!!)
                }
            }
        }

    fun getCharactersInSeries() =
        Pager(
            getDefaultPageConfig(),
            pagingSourceFactory = { CharacterPagingSource(apiService) }
        ).liveData.map {
            val characterMap = mutableSetOf<Long>()
            it.filter { character ->
                if (characterMap.contains(character.id)) {
                    false
                } else {
                    characterMap.add(character.id!!)
                }
            }
        }

    /**
     * let's define page size, page size is the only required param, rest is optional
     */
    fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    companion object {
        val TAG = CharacterRepository::class.java.simpleName
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_PAGE_INDEX = 0
    }
}