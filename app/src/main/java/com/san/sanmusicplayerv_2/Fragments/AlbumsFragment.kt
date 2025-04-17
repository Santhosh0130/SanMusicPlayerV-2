package com.san.sanmusicplayerv_2.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.san.sanmusicplayerv_2.Adapter.AlbumAdapter
import com.san.sanmusicplayerv_2.Model.Album
import com.san.sanmusicplayerv_2.R
import com.san.sanmusicplayerv_2.ViewModel.AlbumViewModel

class AlbumsFragment : Fragment() {

    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var recyclerView: RecyclerView
    private val albumList = mutableListOf<Album>()

    private val albumViewModel: AlbumViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.albumRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        albumAdapter = AlbumAdapter(albumList) { selectedAlbum ->
            // Next step: Navigate to album detail
            val bundel = Bundle().apply {
                putString("albumName", selectedAlbum.title)
                putString("albumArtist", selectedAlbum.artist)
            }

            findNavController().navigate(R.id.albumDetailsFragment, bundel)
        }

        recyclerView.adapter = albumAdapter

        // Fetch and update list
        albumList.clear()
        albumList.addAll(albumViewModel.fetchAlbums(requireContext()))
        albumAdapter.notifyDataSetChanged()
    }
}

