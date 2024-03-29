package com.mauricio.marvel.characters.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mauricio.marvel.R
import com.mauricio.marvel.characters.adapters.CharacterSeriesAdapter
import com.mauricio.marvel.characters.models.Character
import com.mauricio.marvel.characters.models.EXTRA_CHARACTER
import com.mauricio.marvel.characters.models.IOnClickEvent
import com.mauricio.marvel.characters.viewmodels.CharacterViewModel
import com.mauricio.marvel.databinding.ActivityCharacterDetailBinding
import com.mauricio.marvel.utils.Constant.DEFAULT_COLUNS
import com.mauricio.marvel.utils.Constant.LIST_VIEW_FORMAT
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CharacterDetailActivity : AppCompatActivity(), IOnClickEvent {
    private var _binding: ActivityCharacterDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CharacterViewModel>()
    private lateinit var characterAdapter: CharacterSeriesAdapter

    private fun getCharacterFromIntent() = intent.extras?.get(EXTRA_CHARACTER) as Character

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        _binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeParameters()
        initAdapters()
        initObservers()
        getCharacterFromIntent()?.let { character ->
            supportActionBar?.title = character.name
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initializeParameters() {
        with(binding) {
            columns = DEFAULT_COLUNS
            layoutManager = LIST_VIEW_FORMAT
        }
    }

    private fun initAdapters() {
        characterAdapter = CharacterSeriesAdapter(this)
        binding.characterAdapter = characterAdapter
    }

    private fun initObservers() {
        with(viewModel) {
            getCharacterFromIntent().let { character ->
                character.id?.let { id ->
                    charactersEvents(id).observe(this@CharacterDetailActivity) {
                        characterAdapter.submitData(this@CharacterDetailActivity.lifecycle, it)
                    }
                }
            }
        }
    }

    override fun onItemClicked(value: Character) {
        value.urls?.firstOrNull { it.type == "wiki" }?.url?.let { url ->
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                startActivity(this)
            }
        } ?: run {
            Toast.makeText(this, getString(R.string.error_url_not_found), Toast.LENGTH_SHORT).show()
        }
    }
}