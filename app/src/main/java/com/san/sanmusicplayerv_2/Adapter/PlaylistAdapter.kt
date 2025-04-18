package com.san.sanmusicplayerv_2.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.san.sanmusicplayerv_2.Model.Playlist
import com.san.sanmusicplayerv_2.R
import com.san.sanmusicplayerv_2.ViewModel.SongViewModel
import com.san.sanmusicplayerv_2.databinding.ItemPlaylistBinding

class PlaylistAdapter(private val playlists: List<Playlist>,
                      private val songViewModel: SongViewModel,
                      private val onItemLongClick: (Playlist, View) -> Unit,
                      private val onItemClick: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position], position)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    inner class PlaylistViewHolder(val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(playlist: Playlist, position: Int) {
                binding.cardTitle.text = playlist.name ?: "NILL"
                binding.cardArtist.text = "${playlist.songIds.size} Songs"

                val filteredSongs = songViewModel.songs.value!!.filter { song -> song.id in playlist.songIds }
                if (playlist.songIds.isNotEmpty()) {
                    if (playlist.songIds.size >= 4){
                        binding.apply {
                            cardCover1.setImageURI(filteredSongs[0].songCoverImage)
                            cardCover2.setImageURI(filteredSongs[1].songCoverImage)
                            cardCover3.setImageURI(filteredSongs[2].songCoverImage)
                            cardCover4.setImageURI(filteredSongs[3].songCoverImage)

                            singleSongCover.visibility = View.GONE
                        }
                    } else {
                        binding.apply {
                            singleSongCover.apply {
                                visibility = View.VISIBLE
                                setImageURI(filteredSongs[0].songCoverImage)
                            }
                        }
                    }
                } else {
                    binding.apply {
                        singleSongCover.apply {
                            visibility = View.VISIBLE
                            setImageResource(R.drawable.music_note)
                        }
                    }
                }

                binding.root.setOnClickListener{
                    onItemClick(playlist)
                }

                binding.root.setOnLongClickListener {
                    onItemLongClick(playlist, it)
                    true
                }
            }
        }
}
