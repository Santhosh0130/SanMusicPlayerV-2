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
import com.san.sanmusicplayerv_2.Model.Song
import com.san.sanmusicplayerv_2.ViewModel.MusicViewModel
import com.san.sanmusicplayerv_2.ViewModel.SongViewModel
import com.san.sanmusicplayerv_2.databinding.FragmentFavouritesBinding
import com.san.sanmusicplayerv_2.viewmodel.FavouriteViewModel


class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private val favViewModel: FavouriteViewModel by activityViewModels()
    private val songViewModel: SongViewModel by activityViewModels()
    private val viewModel: MusicViewModel by activityViewModels()

    private lateinit var adapter: SongAdapter  // Reuse your SongAdapter

    private var favSongs: List<Song> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        songViewModel.songs.observe(viewLifecycleOwner) { song ->
            favViewModel.getAllFavourites().observe(viewLifecycleOwner) {favIds ->
                favSongs = song.filter { songId -> favIds.contains(songId.id) }

                adapter.updateSongs(favSongs)

            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = SongAdapter { position ->
            viewModel.setSongs(favSongs, position)
            viewModel.playCurrentSong()
        }


        binding.favRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favRecyclerView.adapter = adapter
    }
}
