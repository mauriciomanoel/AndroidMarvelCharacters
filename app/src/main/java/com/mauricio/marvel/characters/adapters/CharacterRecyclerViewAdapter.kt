package com.mauricio.marvel.characters.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mauricio.marvel.BR
import com.mauricio.marvel.characters.models.IOnClickEvent
import com.mauricio.marvel.characters.models.Character
import com.mauricio.marvel.databinding.ItemCharacterBinding

class CharacterRecyclerViewAdapter(private val callback: IOnClickEvent) :
    PagingDataAdapter<Character, CharacterRecyclerViewAdapter.ViewHolder>(CHARACTER_COMPARATOR) {

//    RecyclerView.Adapter<CharacterRecyclerViewAdapter.ViewHolder>() {

//    private val differ = AsyncListDiffer(this, CharactersDiffCallback())
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
        val currentItem = getItem(position)
        currentItem?.run {
            holder.binding.itemCharacter.setOnClickListener {
                callback.onItemClicked(this)
            }
            holder.bind(this)
        }
    }

//    override fun getItemCount() = differ.currentList.size

//    fun submitList(values: List<Character>) {
//        differ.submitList(values)
//    }

    inner class ViewHolder(val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            binding.setVariable(BR.urlPhoto, character.imageUrl)
            binding.setVariable(BR.name, character.getNameFormatted)
            binding.executePendingBindings()
        }
    }

    companion object {
        val TAG = CharacterRecyclerViewAdapter::class.java.canonicalName
        private val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<Character>() {

            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                Log.v(TAG, "oldItem.id= ${oldItem.id} == newItem.id=${newItem.id}")
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                Log.v(TAG, "oldItem.id= ${oldItem.id} == newItem.id=${newItem.id}")

                return oldItem == newItem
            }

            override fun getChangePayload(oldItem: Character, newItem: Character): Any? {

                Log.v(TAG, "oldItem.id= ${oldItem.id} == newItem.id=${newItem.id}")
                return super.getChangePayload(oldItem, newItem)
            }
        }
    }

}



