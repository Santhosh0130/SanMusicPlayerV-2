package com.san.sanmusicplayerv_2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.sanmusicplayerv_2.Model.Song
import com.san.sanmusicplayerv_2.databinding.ItemSongBinding

class FavouriteSongAdapter(
    private val onSongClick: (Int) -> Unit,
) : RecyclerView.Adapter<FavouriteSongAdapter.FavoriteViewHolder>() {

    private var favorites = listOf<Song>()

    inner class FavoriteViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song, position: Int) {
            binding.songName.text = song.songName
            binding.artistName.text = song.artistName
            binding.songCoverImage.setImageURI(song.songCoverImage)

            binding.root.setOnClickListener {
                onSongClick(position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favorites[position], position)
    }

    override fun getItemCount() = favorites.size
}
