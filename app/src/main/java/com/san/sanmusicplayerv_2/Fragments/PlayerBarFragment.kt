package com.san.sanmusicplayerv_2.Fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.san.sanmusicplayerv_2.R
import com.san.sanmusicplayerv_2.ViewModel.MusicViewModel
import com.san.sanmusicplayerv_2.databinding.FragmentPlayerBarBinding

class PlayerBarFragment : Fragment() {

    private var _binding: FragmentPlayerBarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MusicViewModel by activityViewModels()

    private var handler: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        handler = Handler(Looper.getMainLooper())

        viewModel.currentSong.observe(viewLifecycleOwner) { song ->
            if (song != null) {
                binding.songTitle.text = song.songName
                Glide.with(requireContext()).load(song.songCoverImage).into(binding.albumArt)
            }
        }

//        viewModel.songs.observe(viewLifecycleOwner) { songList ->
//            if (viewModel.currentSong.value == null) {
//                binding.songTitle.text = songList.get(getRandom(songList.size - 1)).songName
//                Glide.with(requireContext()).load(songList.get(getRandom(songList.size - 1)).songCoverImage).into(binding.albumArt)
//            }
//        }



        viewModel.isPlaying.observe(viewLifecycleOwner) { playing ->
            binding.playPauseBtn.setImageResource(
                if (playing) R.drawable.pause
                else R.drawable.play
            )

            updateProgressBar()
        }

        binding.playPauseBtn.setOnClickListener {
            viewModel.togglePlayPause()
        }

        binding.btnNext.setOnClickListener{
            viewModel.nextSong()

            updateProgressBar()
        }

        // Optionally: navigate to NowPlayingFragment on click
        binding.root.setOnClickListener {
            // Navigation logic if needed
            findNavController().navigate(R.id.nowPlayingFragment)
            binding.root.visibility = View.VISIBLE
        }
    }

    private fun updateProgressBar() {
        handler?.post(object : Runnable {
            override fun run() {
                val player = viewModel.getMediaPlayer()
                if (player != null && player.isPlaying) {
                    val position = player.currentPosition
                    val duration = player.duration

                    binding.progressBar.max = duration
                    binding.progressBar.progress = position

                    handler?.postDelayed(this, 1000)
                }

            }
        })
    }

    override fun onDestroyView() {
        handler?.removeCallbacksAndMessages(null)
        super.onDestroyView()
        _binding = null
    }
}
