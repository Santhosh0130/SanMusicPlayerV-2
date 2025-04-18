package com.san.sanmusicplayerv_2.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.san.sanmusicplayerv_2.Adapter.SongAdapter
import com.san.sanmusicplayerv_2.Model.Song
import com.san.sanmusicplayerv_2.R
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

    private val favSongsLiveData = MutableLiveData<List<Song>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = SongAdapter { position ->
            favSongsLiveData.value?.let { viewModel.setSongs(it, position) }
            viewModel.playCurrentSong()
        }

        binding.favRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favRecyclerView.adapter = adapter

        songViewModel.songs.observe(viewLifecycleOwner) { songs ->
            favViewModel.getAllFavourites().observe(viewLifecycleOwner) { favIds ->
                val updatedFavSongs = songs.filter { song -> favIds.contains(song.id) }
                favSongsLiveData.value = updatedFavSongs  // Update the LiveData
            }
        }

        favSongsLiveData.observe(viewLifecycleOwner) { updatedFavSongs ->
            adapter.updateSongs(updatedFavSongs)
            updateUI(updatedFavSongs)
        }
    }

    private fun updateUI(favSongs: List<Song>) {
        if (favSongs.isEmpty()) {
            binding.emptyImage.visibility = View.VISIBLE
            binding.favRecyclerView.visibility = View.GONE
        } else {
            binding.emptyImage.visibility = View.GONE
            binding.favRecyclerView.visibility = View.VISIBLE
        }
    }

}