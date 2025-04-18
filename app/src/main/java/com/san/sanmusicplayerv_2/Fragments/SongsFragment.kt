package com.san.sanmusicplayerv_2.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.san.sanmusicplayerv_2.Adapter.SongAdapter
import com.san.sanmusicplayerv_2.ViewModel.MusicViewModel
import com.san.sanmusicplayerv_2.ViewModel.SongViewModel
import com.san.sanmusicplayerv_2.databinding.FragmentSongsBinding

class SongsFragment : Fragment() {

    private var _binding: FragmentSongsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MusicViewModel by activityViewModels()
    private val songViewModel: SongViewModel by activityViewModels()
    private lateinit var adapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SongAdapter { index ->
            viewModel.setSongs(songViewModel.songs.value ?: emptyList(), index)
            viewModel.playCurrentSong()
//            findNavController().navigate(R.id.nowPlayingFragment)
        }

        binding.songsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.songsRecyclerView.adapter = adapter

        binding.shuffleBtn.setOnClickListener{
            viewModel.setSongs(songViewModel.songs.value ?: emptyList(), 0)
            viewModel.playRandomSongs()
        }

        songViewModel.songs.observe(viewLifecycleOwner) { songList ->
            adapter.updateSongs(songList)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


