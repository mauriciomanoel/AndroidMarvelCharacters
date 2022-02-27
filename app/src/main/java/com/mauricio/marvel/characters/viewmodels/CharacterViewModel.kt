package com.mauricio.marvel.characters.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mauricio.marvel.characters.models.Character
import com.mauricio.marvel.characters.models.CharacterEvents
import com.mauricio.marvel.characters.repositories.CharacterRepository
import com.mauricio.marvel.utils.Constant.SERIES_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(val repository: CharacterRepository) : ViewModel() {

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> = _characters

    private val _charactersEvents = MutableLiveData<List<Character>>()
    val charactersEvents: LiveData<List<Character>> = _charactersEvents

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError

    fun getCharactersInSeries() {
        showLoading()
        repository.getCharactersInSeries(SERIES_ID, ::processCharacters)
    }

    fun getCharacterEvents(characterId: Long) {
        showLoading()
        repository.getCharacterEvents(characterId, ::processCharacterEvents)
    }

    private fun processCharacters(results: List<Character>?, e: Throwable?) {
        hideLoading()
        results?.let {
            _characters.apply {
                postValue(it)
            }
        } ?: run {
            _messageError.apply {
                postValue(e?.message)
            }
        }
        Log.v("TAG", "$results")
    }

    private fun processCharacterEvents(results: List<Character>?, e: Throwable?) {
        hideLoading()
        results?.let {
            _charactersEvents.apply {
                postValue(it)
            }
        } ?: run {
            _messageError.apply {
                postValue(e?.message)
            }
        }
        Log.v("TAG", "$results")
    }

    private fun showLoading() {
        _showLoading.apply {
            postValue(true)
        }
    }

    private fun hideLoading() {
        _showLoading.apply {
            postValue(false)
        }
    }

}