package com.mauricio.marvel.characters.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.mauricio.marvel.characters.models.Character
import com.mauricio.marvel.characters.repositories.CharacterRepository
import com.mauricio.marvel.utils.Constant.SERIES_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(val repository: CharacterRepository) : ViewModel() {

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> = _characters

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> = _messageError

    fun getCharactersInSeries() {
        showLoading()
        repository.getCharactersInSeries(SERIES_ID, ::processCharacters)
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

    fun charactersEvents(characterId:Long) = repository.getCharacterEvents(characterId).cachedIn(viewModelScope)

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