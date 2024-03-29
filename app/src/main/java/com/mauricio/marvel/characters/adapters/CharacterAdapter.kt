package com.mauricio.marvel.characters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.marvel.BR
import com.mauricio.marvel.characters.models.IOnClickEvent
import com.mauricio.marvel.characters.models.Character
import com.mauricio.marvel.databinding.ItemCharacterBinding

class CharacterAdapter(private val callback: IOnClickEvent) : RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    private val differ = AsyncListDiffer(this, CharactersDiffCallback())
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterAdapter.ViewHolder {
        return ViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CharacterAdapter.ViewHolder, position: Int) {
        differ.currentList[position].run {
            holder.binding.itemCharacter.setOnClickListener {
                callback.onItemClicked(this)
            }
            holder.bind(this)
        }
    }

    override fun getItemCount() = differ.currentList.size

    fun submitList(values: List<Character>) {
        differ.submitList(values)
    }

    inner class ViewHolder(var binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.setVariable(BR.urlPhoto, character.imageUrl)
            binding.setVariable(BR.name, character.getNameFormatted)
            binding.executePendingBindings()
        }
    }
}

class CharactersDiffCallback: DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return (oldItem as Character).id == (newItem as Character).id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return (oldItem as Character) == (newItem as Character)
    }
}