package com.mauricio.marvel.characters.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mauricio.marvel.R
import com.mauricio.marvel.characters.adapters.CharacterRecyclerViewAdapter
import com.mauricio.marvel.characters.models.Character
import com.mauricio.marvel.characters.models.EXTRA_CHARACTER
import com.mauricio.marvel.characters.models.IOnClickEvent
import com.mauricio.marvel.characters.viewmodels.CharacterViewModel
import com.mauricio.marvel.databinding.ActivityCharacterDetailBinding
import com.mauricio.marvel.utils.Constant
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CharacterDetailActivity : AppCompatActivity(), IOnClickEvent {
    private var _binding: ActivityCharacterDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CharacterViewModel>()
    private lateinit var characterAdapter: CharacterRecyclerViewAdapter

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
            character.id?.let { id ->
                viewModel.getCharacterEvents(id)
            }
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
            columns = Constant.DEFAULT_COLUNS
            layoutManager = Constant.LIST_VIEW_FORMAT
        }
    }

    private fun initAdapters() {
        characterAdapter = CharacterRecyclerViewAdapter(this)
        binding.characterAdapter = characterAdapter
    }

    private fun initObservers() {
        with(viewModel) {
            charactersEvents.observe(this@CharacterDetailActivity) {
                characterAdapter.submitList(it)
            }
            showLoading.observe(this@CharacterDetailActivity) { showLoading ->
                binding.showLoading = showLoading
            }
            messageError.observe(this@CharacterDetailActivity) { message ->
                Toast.makeText(this@CharacterDetailActivity, message, Toast.LENGTH_SHORT).show()
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