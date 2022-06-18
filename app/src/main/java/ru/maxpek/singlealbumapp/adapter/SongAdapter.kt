package ru.maxpek.singlealbumapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxpek.singlealbumapp.databinding.CompositionCardBinding
import ru.maxpek.singlealbumapp.dto.Song

interface AdapterCallback {
   fun onPlay(song: Song)
   fun onPause()
}

class SongAdapter (private val callback: AdapterCallback) :
    ListAdapter<Song, SongViewHolder>(MarkerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = CompositionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder (binding, callback)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class SongViewHolder
    (private val binding: CompositionCardBinding,
     private val callback: AdapterCallback)  : RecyclerView.ViewHolder(binding.root) {

    fun bind(song: Song) {
        binding.apply {
            play.isChecked = false
            artist.text = song.author
            title.text = song.file
            play.isChecked = song.reproduced
            play.setOnClickListener {
                if (play.isChecked) callback.onPlay(song) else callback.onPause()
            }
        }
    }
}

class MarkerDiffCallback : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }
}