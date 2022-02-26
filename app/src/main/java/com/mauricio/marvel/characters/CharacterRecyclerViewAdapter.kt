package com.mauricio.marvel.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.marvel.BR
import com.mauricio.marvel.databinding.ItemCharacterBinding

class CharacterRecyclerViewAdapter(private val callback: IOnClickEvent) : RecyclerView.Adapter<CharacterRecyclerViewAdapter.ViewHolder>() {

    val differ = AsyncListDiffer(this, CharactersDiffCallback())
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterRecyclerViewAdapter.ViewHolder {
        return ViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CharacterRecyclerViewAdapter.ViewHolder, position: Int) {
        differ.currentList[position].run {
            holder.binding.itemCharacter.setOnClickListener {
                callback.onItemClicked(this)
            }
            holder.bind(this)
        }
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder(var binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.setVariable(BR.urlPhoto, character.imageUrl())
            binding.setVariable(BR.name, character.name)
            binding.executePendingBindings()
        }
    }
}

class CharactersDiffCallback: DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }
}