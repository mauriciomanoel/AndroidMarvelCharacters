package com.mauricio.marvel

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.mauricio.marvel.characters.models.Character
import com.mauricio.marvel.characters.adapters.CharacterRecyclerViewAdapter
import com.mauricio.marvel.characters.models.EXTRA_CHARACTER
import com.mauricio.marvel.characters.viewmodels.CharacterViewModel
import com.mauricio.marvel.characters.models.IOnClickEvent
import com.mauricio.marvel.characters.views.CharacterDetailActivity
import com.mauricio.marvel.databinding.ActivityMainBinding
import com.mauricio.marvel.utils.Constant.DEFAULT_COLUNS
import com.mauricio.marvel.utils.Constant.GRID_VIEW_FORMAT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), IOnClickEvent {

    private val viewModel by viewModels<CharacterViewModel>()
    private lateinit var characterAdapter: CharacterRecyclerViewAdapter
    private lateinit var activity: Activity
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeParameters()
        initAdapters()
        initObservers()
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
//        Log.v("main", value.toString())
        Intent(this, CharacterDetailActivity::class.java).apply {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_CHARACTER, value as Parcelable)
            putExtras(bundle)
            startActivity(this)
        }
    }

}