package com.san.sanmusicplayerv_2.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.san.sanmusicplayerv_2.Model.Album
import com.san.sanmusicplayerv_2.R

class AlbumAdapter(
    private val albumList: List<Album>,
    private val onAlbumClick: (Album) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumArt: ImageView = view.findViewById(R.id.card_cover)
        val albumName: TextView = view.findViewById(R.id.card_title)
        val albumArtist: TextView = view.findViewById(R.id.card_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_card, parent, false)
        return AlbumViewHolder(view)
    }

    override fun getItemCount() = albumList.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albumList[position]
        holder.albumName.text = album.title
        holder.albumArtist.text = album.artist
        holder.albumArt.setImageURI(album.albumCover)

        holder.itemView.setOnClickListener {
            onAlbumClick(album)
        }
    }
}
