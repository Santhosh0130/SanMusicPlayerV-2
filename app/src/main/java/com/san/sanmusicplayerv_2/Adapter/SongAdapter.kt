package com.san.sanmusicplayerv_2.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.sanmusicplayerv_2.Model.Song
import com.san.sanmusicplayerv_2.databinding.ItemSongBinding

class SongAdapter(
    private val onSongClick: (Int) -> Unit,
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private var songs: List<Song> = emptyList()

    fun updateSongs(newSongs: List<Song>) {
        this.songs = newSongs
        notifyDataSetChanged()
    }

    inner class SongViewHolder(private val binding: ItemSongBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position], position)
    }

    override fun getItemCount() = songs.size

}
