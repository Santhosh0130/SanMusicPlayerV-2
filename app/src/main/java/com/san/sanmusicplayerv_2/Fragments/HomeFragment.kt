package com.san.sanmusicplayerv_2.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.san.sanmusicplayerv_2.Adapter.HomeListAdapter
import com.san.sanmusicplayerv_2.Adapter.SongAdapter
import com.san.sanmusicplayerv_2.R
import com.san.sanmusicplayerv_2.ViewModel.MusicViewModel
import com.san.sanmusicplayerv_2.ViewModel.SongViewModel
import com.san.sanmusicplayerv_2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MusicViewModel by activityViewModels()
    private val songViewModel: SongViewModel by activityViewModels()

    private lateinit var recentAdapter: HomeListAdapter
    private lateinit var mostAdapter: HomeListAdapter
    private lateinit var searchAdapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setGreeting()

        recentAdapter = HomeListAdapter{ index ->
            viewModel.listOfSongs.value?.let {
                viewModel.setSongs(it, index)
            }
            viewModel.playCurrentSong()
        }

        mostAdapter = HomeListAdapter{ index ->
            viewModel.mostPlayedSongs.value?.let {
                viewModel.setSongs(it, index)
            }
            viewModel.playCurrentSong()
        }

        searchAdapter = SongAdapter { index ->
            binding.searchEditText.text = null
            hideKeyboard()
            viewModel.filteredSongs.value?.let { viewModel.setSongs(it, index) }
            viewModel.playCurrentSong()
        }

        binding.apply {

            recentlyPlayedRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = recentAdapter
            }

            mostPlayedRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = mostAdapter
            }

            searchRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = searchAdapter
            }

            searchBox.setOnClickListener {
                binding.apply {
                    homeLayout.visibility = View.GONE
                    searchTitle.visibility = View.GONE
                    searchEditText.visibility = View.VISIBLE
                    searchRecyclerView.visibility = View.VISIBLE

                    searchIcon.apply {
                        setImageResource(R.drawable.arrow_back)
                        setOnClickListener{
                            homeLayout.visibility = View.VISIBLE
                            searchTitle.visibility = View.VISIBLE
                            searchEditText.visibility = View.GONE
                            searchRecyclerView.visibility = View.GONE
                            binding.searchEditText.text = null
                            hideKeyboard()

                            searchIcon.setImageResource(R.drawable.search)
                        }
                    }
                }
            }

            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val query = p0.toString().trim()
                    if (query.isNotEmpty()) {
                        songViewModel.songs.observe(viewLifecycleOwner) { songs ->
                            viewModel.filterSong(songs, query)
                        }

                        binding.searchRecyclerView.adapter = searchAdapter
                        viewModel.filteredSongs.observe(viewLifecycleOwner) {
                            searchAdapter.updateSongs(it)
                        }
                    } else {
                        binding.searchRecyclerView.adapter = searchAdapter
                        songViewModel.songs.observe(viewLifecycleOwner) {
                            searchAdapter.updateSongs(it)
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}

            })
        }

        // 🔁 When all songs load from MediaStore, call getSongsByIds()
        songViewModel.songs.observe(viewLifecycleOwner) { songList ->
            viewModel.getSongsByIds(songList)
            viewModel.getMostPlayedSongs(songList)
        }


        // 🔁 Observe recently played (listOfSongs) and update the adapter
        viewModel.listOfSongs.observe(viewLifecycleOwner) { recentList ->
            recentAdapter.updateSongs(recentList)

        if (viewModel.listOfSongs.value.isNullOrEmpty()) {
            binding.apply {
                emptyHomeListText1.visibility = View.VISIBLE
                emptyHomeListText2.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                emptyHomeListText1.visibility = View.GONE
                emptyHomeListText2.visibility = View.GONE
            }
        }
        }

        // 🔁 Observe recently played (listOfSongs) and update the adapter
        viewModel.mostPlayedSongs.observe(viewLifecycleOwner) { mostList ->
            mostAdapter.updateSongs(mostList)
        }
    }

    private fun setGreeting() {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }

        binding.welcomeTitle.text = greeting
    }


    private fun hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}

