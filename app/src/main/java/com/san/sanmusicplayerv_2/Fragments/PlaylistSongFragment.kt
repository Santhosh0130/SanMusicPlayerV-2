package com.san.sanmusicplayerv_2.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.san.sanmusicplayerv_2.Adapter.SongAdapter
import com.san.sanmusicplayerv_2.ViewModel.MusicViewModel
import com.san.sanmusicplayerv_2.ViewModel.SongViewModel
import com.san.sanmusicplayerv_2.databinding.FragmentAlbumSongsBinding

class PlaylistSongFragment : Fragment() {

    private lateinit var binding: FragmentAlbumSongsBinding
    private val songViewModel: SongViewModel by activityViewModels()
    private val viewModel: MusicViewModel by activityViewModels()
    private lateinit var adapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val playlistName = arguments?.getString("playlistName") ?: return
        val songIds = arguments?.getLongArray("songIds") ?: return
        val allSongs = songViewModel.songs.value // Fetch all songs

        Toast.makeText(requireContext(), "${songIds} ${allSongs?.size}", Toast.LENGTH_SHORT).show()

        // Filter songs based on songIds passed from the playlist
        val playlistSong = allSongs!!.filter { song -> song.id in songIds }

        val playlistSongs = allSongs.filter { song -> song.id in songIds }
        Toast.makeText(requireContext(), "${playlistSongs.size}", Toast.LENGTH_SHORT).show()

        // Set up the adapter
        adapter = SongAdapter { index ->
            viewModel.setSongs(playlistSongs, index)
            viewModel.playCurrentSong()
        }

        adapter.updateSongs(playlistSongs)

        binding.albumSongsRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.albumSongsRecyclerview.adapter = adapter

        binding.albumTitle.text = playlistName
        binding.totalSongs.text = "${playlistSongs.size} Songs"

        binding.playAll.setOnClickListener {
            if (playlistSongs.isNotEmpty()){
                viewModel.setSongs(playlistSongs, 0)
                viewModel.playRandomSongs()
            } else {
                Toast.makeText(requireContext(), "No Songs to play!.", Toast.LENGTH_SHORT).show()
            }
        }

        playlistSongs.firstOrNull()?.let { song ->
            binding.albumCover.setImageURI(song.songCoverImage)
        }
        binding.albumArtist.visibility = View.GONE
    }
}
