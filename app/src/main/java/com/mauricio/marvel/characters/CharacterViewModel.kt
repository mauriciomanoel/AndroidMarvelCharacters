package com.mauricio.marvel.characters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
        repository.getCharactersInSeries(::processCharacters)
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