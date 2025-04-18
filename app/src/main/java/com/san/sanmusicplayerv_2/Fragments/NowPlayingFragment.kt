package com.san.sanmusicplayerv_2.Fragments

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.san.sanmusicplayerv_2.Model.Playlist
import com.san.sanmusicplayerv_2.R
import com.san.sanmusicplayerv_2.ViewModel.MusicViewModel
import com.san.sanmusicplayerv_2.ViewModel.PlaylistViewModel
import com.san.sanmusicplayerv_2.ViewModel.SongViewModel
import com.san.sanmusicplayerv_2.databinding.FragmentNowPlayingBinding
import com.san.sanmusicplayerv_2.viewmodel.FavouriteViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class NowPlayingFragment : Fragment() {
    private var _binding: FragmentNowPlayingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MusicViewModel by activityViewModels()
    private val songViewModel: SongViewModel by activityViewModels()
    private val favViewModel: FavouriteViewModel by activityViewModels()
    private val playlistViewModel: PlaylistViewModel by activityViewModels()

    private var currentSongId: Long = 0L

    private var cachedPlaylists: List<Playlist> = emptyList()

    private var isFavourite: Boolean = false


    private var handler: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNowPlayingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val audiManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager

        handler = Handler(Looper.getMainLooper())

        viewModel.currentSong.observe(viewLifecycleOwner) { song ->
            binding.songTitle.text = song?.songName
            binding.artistName.text = song?.artistName
            binding.songCover.setImageURI(song?.songCoverImage)

            song?.let { currentSongId = it.id }
        }

        viewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            binding.btnPlayPause.setImageResource(
                if (isPlaying) R.drawable.pause
                else R.drawable.play
            )
            updateSeekBar()
        }

        binding.btnPlayPause.setOnClickListener {
            viewModel.togglePlayPause()
        }

        binding.btnNext.setOnClickListener {
            viewModel.nextSong()
        }

        binding.btnPrevious.setOnClickListener {
            viewModel.previousSong()
        }

        binding.volumeUp.setOnClickListener {
            audiManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND)
        }

        binding.volumeDown.setOnClickListener {
            audiManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND)
        }

        playlistViewModel.allPlaylists.observe(viewLifecycleOwner) { playlists ->
            cachedPlaylists = playlists
        }

        binding.addToPlaylist.setOnClickListener {
            if (cachedPlaylists.isNotEmpty()) {
                val playlistNames = cachedPlaylists.map { it.name }.toTypedArray()

                AlertDialog.Builder(requireContext())
                    .setTitle("Add to Playlist")
                    .setItems(playlistNames) { _, index ->
                        val selectedPlaylist = cachedPlaylists[index]
                        val currentSongId = currentSongId  // make sure this is correctly set

                        val updatedSongIds = selectedPlaylist.songIds.toMutableList().apply {
                            if (!contains(currentSongId)) add(currentSongId)
                        }

                        val updatedPlaylist = selectedPlaylist.copy(songIds = updatedSongIds)
                        playlistViewModel.updatePlaylist(updatedPlaylist)

                        Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            } else {
                Toast.makeText(requireContext(), "Create playlist first", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.currentSong.observe(viewLifecycleOwner) { song ->
            song?.let{
                checkAndUpdateFavouriteIcon(it.id)
                setupFavouriteToggle(it.id)
            }
        }

    }

    private fun checkAndUpdateFavouriteIcon(songId: Long) {
        lifecycleScope.launch {
            isFavourite = favViewModel.isFavourite(songId)
            updateHeartIcon(isFavourite)
        }

    }

    private fun setupFavouriteToggle(songId: Long) {
        binding.favBtn.setOnClickListener {
            if (isFavourite) {
                favViewModel.removeFromFavourites(songId)
                isFavourite = false
            } else {
                favViewModel.addToFavourites(songId)
                isFavourite = true
            }
            updateHeartIcon(isFavourite)
        }
    }

    private fun updateHeartIcon(isFav: Boolean) {
        val drawableRes = if (isFav) R.drawable.favorite else R.drawable.favorite_blank
        binding.favBtn.setImageDrawable(ContextCompat.getDrawable(requireContext(), drawableRes))
    }

    private fun updateSeekBar() {
        handler?.post(object : Runnable {
            override fun run() {
                val player = viewModel.getMediaPlayer()
                if (player != null && player.isPlaying) {
                    val position = player.currentPosition
                    val duration = player.duration

                    binding.seekBar.max = duration
                    binding.seekBar.progress = position
                    binding.currentTime.text = formatTime(position)
                    binding.totalTime.text = formatTime(duration)

                    handler?.postDelayed(this, 1000)
                }
            }
        })
    }

    private fun formatTime(ms: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(ms.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(ms.toLong()) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroyView() {
        handler?.removeCallbacksAndMessages(null)
        _binding = null
        super.onDestroyView()
    }
}
