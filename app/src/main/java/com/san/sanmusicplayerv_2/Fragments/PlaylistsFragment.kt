package com.san.sanmusicplayerv_2.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.san.sanmusicplayerv_2.Adapter.PlaylistAdapter
import com.san.sanmusicplayerv_2.Model.Playlist
import com.san.sanmusicplayerv_2.R
import com.san.sanmusicplayerv_2.ViewModel.PlaylistViewModel
import com.san.sanmusicplayerv_2.ViewModel.SongViewModel
import com.san.sanmusicplayerv_2.databinding.FragmentPlaylistsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private lateinit var playlistViewModel: PlaylistViewModel
    private val viewModel: SongViewModel by activityViewModels()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        playlistViewModel = ViewModelProvider(this).get(PlaylistViewModel::class.java)

        // Observe live data from the ViewModel and update the adapter
        playlistViewModel.allPlaylists.observe(viewLifecycleOwner) { playlists ->
            val adapter = PlaylistAdapter(
                playlists, viewModel,
                onItemLongClick = {playlist, view ->
                    showPopup(playlist, view)
                },
                onItemClick = {playlist ->
                    val bundle = Bundle().apply {
                        putString("playlistName", playlist.name)
                        putLongArray("songIds", playlist.songIds.toLongArray())
                    }
                    findNavController().navigate(R.id.action_playlistsFragment_to_playlistSongsFragment, bundle)
                }
            )

            // Set up RecyclerView with ViewBinding
            binding.playlistRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.playlistRecyclerView.adapter = adapter
        }


        // Handle "Add Playlist" button click using ViewBinding
        binding.addPlaylist.setOnClickListener {
            showAddPlaylistDialog()
        }
    }

    private fun showPopup(playlist: Playlist, anchorView: View) {
        val popupMenu = PopupMenu(requireContext(), anchorView)
        popupMenu.menuInflater.inflate(R.menu.playlist_popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.rename -> {
                    renameMenu(playlist)
                    true
                }
                R.id.delete -> {
                    playlistViewModel.deletePlaylist(playlist)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun renameMenu(playlist: Playlist) {
        val editText = EditText(requireContext())
        editText.setText(playlist.name)

        AlertDialog.Builder(requireContext())
            .setTitle("Rename Playlist")
            .setView(editText)
            .setPositiveButton("Rename") { _, _ ->
                val newName = editText.text.toString()
                if (newName.isNotBlank()) {
                    val updatedPlaylist = playlist.copy(name = newName)
                    playlistViewModel.updatePlaylist(updatedPlaylist)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Function to show the Add Playlist dialog
    private fun showAddPlaylistDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.playlist_dialog, null)
        val editText = dialogView.findViewById<EditText>(R.id.playlistNameEditText)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Add Playlist")
            .setView(dialogView)
            .setCancelable(true)
            .setPositiveButton("Save") { dialogInterface, _ ->
                val playlistName = editText.text.toString().trim()
                if (playlistName.isNotEmpty()) {
                    val playlist = Playlist(name = playlistName)
                    // Insert new playlist into the database
                    CoroutineScope(Dispatchers.IO).launch {
                        playlistViewModel.insertPlaylist(playlist)
                    }
                }
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference when the view is destroyed
    }
}
