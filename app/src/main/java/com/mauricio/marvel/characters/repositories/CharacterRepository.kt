package com.mauricio.marvel.characters.repositories

import android.util.Log
import com.mauricio.marvel.characters.models.Character
import com.mauricio.marvel.characters.models.CharacterEvents
import com.mauricio.marvel.network.RetrofitApiService
import com.mauricio.marvel.utils.Constant.SERIES_ID
import kotlinx.coroutines.*
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

    fun getCharacterEvents(characterId:Long, process: (value: List<Character>?, e: Throwable?) -> Unit) {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "CoroutineExceptionHandler got $exception")
            process(null, exception)
        }
        val job = coroutineScope.launch(handler) {
            val result = apiService.getCharacterEvents(characterId)
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

    fun cancelAllJobs() {
        jobs.forEach {
            if (it.isActive) {
                it.cancel()
            }
        }
    }

    companion object {
        val TAG = CharacterRepository::class.java.simpleName
    }
}