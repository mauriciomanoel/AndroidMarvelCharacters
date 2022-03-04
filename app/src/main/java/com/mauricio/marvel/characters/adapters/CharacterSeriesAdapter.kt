package com.mauricio.marvel.characters.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.marvel.BR
import com.mauricio.marvel.characters.models.IOnClickEvent
import com.mauricio.marvel.characters.models.Character
import com.mauricio.marvel.databinding.ItemCharacterBinding

class CharacterSeriesAdapter(private val callback: IOnClickEvent?) :
    PagingDataAdapter<Character, CharacterSeriesAdapter.ViewHolder>(CHARACTER_COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterSeriesAdapter.ViewHolder {
        return ViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CharacterSeriesAdapter.ViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.run {
            holder.binding.itemCharacter.setOnClickListener {
                callback?.onItemClicked(this)
            }
            holder.bind(this)
        }
    }

    inner class ViewHolder(val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.setVariable(BR.urlPhoto, character.imageUrl)
            binding.setVariable(BR.name, character.getNameFormatted)
            binding.executePendingBindings()
        }
    }

    companion object {
        val TAG = CharacterSeriesAdapter::class.java.canonicalName
        private val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<Character>() {

            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                Log.v(TAG, "oldItem.id= ${oldItem.id} == newItem.id=${newItem.id}")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                Log.v(TAG, "oldItem.id= ${oldItem.id} == newItem.id=${newItem.id}")
                return oldItem == newItem
            }
        }
    }
}



