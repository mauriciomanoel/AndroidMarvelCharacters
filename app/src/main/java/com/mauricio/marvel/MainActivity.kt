package com.mauricio.marvel

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.mauricio.marvel.characters.Character
import com.mauricio.marvel.characters.CharacterRecyclerViewAdapter
import com.mauricio.marvel.characters.CharacterViewModel
import com.mauricio.marvel.characters.IOnClickEvent
import com.mauricio.marvel.databinding.ActivityMainBinding
import com.mauricio.marvel.utils.Constant.DEFAULT_COLUNS
import com.mauricio.marvel.utils.Constant.GRID_VIEW_FORMAT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IOnClickEvent {

    private val viewModel by viewModels<CharacterViewModel>()
    private var _binding: ActivityMainBinding? = null
    private lateinit var characterAdapter: CharacterRecyclerViewAdapter
    private lateinit var activity: Activity
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeParameters()
        initAdapters()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCharactersInSeries()
    }

    private fun initializeParameters() {
        activity = this
        with(binding) {
            columns = DEFAULT_COLUNS
            layoutManager = GRID_VIEW_FORMAT
        }
    }

    private fun initAdapters() {
        characterAdapter = CharacterRecyclerViewAdapter(this)
        binding.breedsAdapter = characterAdapter
    }

    private fun initObservers() {
        with(viewModel) {
            characters.observe(this@MainActivity) {
                characterAdapter.differ.submitList(it)
            }
            showLoading.observe(this@MainActivity) { showLoading ->
                binding.showLoading = showLoading
            }
            messageError.observe(this@MainActivity) { message ->
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClicked(value: Character) {
        Log.v("main", value.toString())
    }

}