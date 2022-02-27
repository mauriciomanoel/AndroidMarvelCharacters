package com.mauricio.marvel.characters.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.mauricio.marvel.R
import com.mauricio.marvel.characters.models.EXTRA_CHARACTER
import com.mauricio.marvel.databinding.ActivityCharacterDetailBinding
import com.mauricio.marvel.databinding.ActivityMainBinding

class CharacterDetailActivity : AppCompatActivity() {
    private var _binding: ActivityCharacterDetailBinding? = null
    private val binding get() = _binding!!

    private fun getCharacterFromIntent() = intent.extras?.get(EXTRA_CHARACTER)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        _binding = ActivityCharacterDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)
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
}