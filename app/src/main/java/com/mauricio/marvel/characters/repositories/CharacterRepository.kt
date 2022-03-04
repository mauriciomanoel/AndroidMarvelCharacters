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
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(private val apiService: RetrofitApiService) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val jobs = mutableListOf<Job>()

    fun getCharactersInSeries(seriesId:String, process: (value: List<Character>?, e: Throwable?) -> Unit) {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "CoroutineExceptionHandler got $exception")
            process(null, exception)
        }
        val job = coroutineScope.launch(handler) {
            val result = apiService.getCharactersInSeries(seriesId)
            process(result.data.results, null)
        }
        jobs.add(job)
        job.invokeOnCompletion { exception: Throwable? ->
            exception?.let {
                Log.e(TAG, "JobCancellationException got $exception")
                process(null, exception)
            }
        }
    }

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

    fun cancelAllJobs() {
        jobs.forEach {
            if (it.isActive) {
                it.cancel()
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