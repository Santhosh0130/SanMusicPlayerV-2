package com.san.sanmusicplayerv_2.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.san.sanmusicplayerv_2.Model.Song
import com.san.sanmusicplayerv_2.databinding.ItemSongCardBinding

class HomeListAdapter(private val onSongClick: (Int) -> Unit): RecyclerView.Adapter<HomeListAdapter.ViewHolder>() {

    private var listOfSongs = listOf<Song>()

    fun updateSongs(newSongs: List<Song>) {
        this.listOfSongs = newSongs
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemSongCardBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song, position: Int) {
            binding.cardTitle.text = song.songName
            binding.cardArtist.visibility = View.GONE
            binding.cardCover.setImageURI(song.songCoverImage)

            binding.root.setOnClickListener {
                onSongClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSongCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOfSongs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listOfSongs[position], position)
    }
}