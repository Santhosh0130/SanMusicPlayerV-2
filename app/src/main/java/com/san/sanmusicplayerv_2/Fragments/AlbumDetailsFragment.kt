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

class AlbumDetailsFragment : Fragment() {

    private lateinit var binding: FragmentAlbumSongsBinding
    private val viewModel: MusicViewModel by activityViewModels()
    private val songViewModel: SongViewModel by activityViewModels()
    private lateinit var adapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val albumName = arguments?.getString("albumName") ?: return
        val albumArtist = arguments?.getString("albumArtist") ?: return

        val allSongs = songViewModel.songs.value ?: emptyList()
        val albumSongs = allSongs.filter { it.album == albumName }

        adapter = SongAdapter { index ->
            viewModel.setSongs(albumSongs, index)
            viewModel.playCurrentSong()
        }
        adapter.updateSongs(albumSongs)

        binding.albumSongsRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        binding.albumSongsRecyclerview.adapter = adapter
        binding.albumTitle.text = albumName
        binding.albumArtist.text = albumArtist
        binding.totalSongs.text = "${albumSongs.size} Songs"
        binding.playAll.setOnClickListener {
            viewModel.setSongs(albumSongs, 0)
            viewModel.playRandomSongs()
        }

        albumSongs.firstOrNull()?.let { song ->
            binding.albumCover.setImageURI(song.songCoverImage)
        }

    }
}

