package com.san.sanmusicplayerv_2.ViewModel

import android.app.Application
import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.san.sanmusicplayerv_2.Model.Song
import com.san.sanmusicplayerv_2.HomeListManager

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private var mediaPlayer: MediaPlayer? = null

    private val _songs = mutableListOf<Song>()  // Keep this
    private val _songsLiveData = MutableLiveData<List<Song>>()  // Add this
    val songs: LiveData<List<Song>> get() = _songsLiveData

    private var currentIndex = -1

    private val _currentSong = MutableLiveData<Song?>()
    val currentSong: LiveData<Song?> get() = _currentSong

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    private val _isShuffle = MutableLiveData<Boolean>()
    val isShuffle: LiveData<Boolean> get() = _isShuffle

    private val _listOfSongs = MutableLiveData<List<Song>>()
    val listOfSongs: LiveData<List<Song>> get() = _listOfSongs

    private val _mostPlayedSongs = MutableLiveData<List<Song>>()
    val mostPlayedSongs: LiveData<List<Song>> get() = _mostPlayedSongs

    private val _filteredSongs = MutableLiveData<List<Song>>()
    val filteredSongs: LiveData<List<Song>> get() = _filteredSongs


    fun setSongs(songList: List<Song>, startIndex: Int = 0) {
        _songs.clear()
        _songs.addAll(songList)
        _songsLiveData.value = _songs.toList()
        currentIndex = startIndex
        _currentSong.value = _songs.getOrNull(currentIndex)
    }

    fun playCurrentSong() {
        val song = _songs.getOrNull(currentIndex) ?: return

        startSong(song)
        _currentSong.value = song
        _isPlaying.value = true
        _isShuffle.value = false

        val manager = HomeListManager(getApplication())
        manager.addSong(song.id)
        manager.increasePlayCount(song.id)
    }

    fun getSongsByIds(allSongs: List<Song>) {
        val songMap = allSongs.associateBy { it.id }
        val manager = HomeListManager(getApplication())
        val recentIds = manager.getRecentlyPlayed()

        val matchedSongs = recentIds.mapNotNull { songMap.get(it) }

        _listOfSongs.value = matchedSongs
    }

    fun getMostPlayedSongs(allSongs: List<Song>) {
        val songMap = allSongs.associateBy { it.id }
        val manager = HomeListManager(getApplication())

        _mostPlayedSongs.value = manager.getMostPlayed().mapNotNull { (id, _) ->
            songMap.get(id)
        }
    }

    fun filterSong(allSongs: List<Song>, query: String) {
        _filteredSongs.value = allSongs.filter {
            it.songName.contains(query, ignoreCase = true) ||
                    it.artistName.contains(query, ignoreCase = true)
        }
    }

    private fun getRandom(size: Int): Int {
        return (0..size).random()
    }

    fun playRandomSongs() {
        val song = _songs.getOrNull(getRandom(_songs.size)) ?: return

        startSong(song)
        _currentSong.value = song
        _isPlaying.value = true
        _isShuffle.value = true

        val manager = HomeListManager(getApplication())
        manager.addSong(song.id)
        manager.increasePlayCount(song.id)
    }

    private fun startSong(song: Song) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.songPath)
            prepare()
            start()
            setOnCompletionListener {
                nextSong()
            }
        }
    }

    fun togglePlayPause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
            } else {
                it.start()
                _isPlaying.value = true
            }
        }
    }

    fun nextSong() {
        if (_songs.isEmpty()) return

        if (isShuffle.value == true) currentIndex = getRandom(_songs.size)
        else currentIndex = (currentIndex + 1) % _songs.size

        playCurrentSong()
    }

    fun previousSong() {
        if (_songs.isEmpty()) return

        currentIndex = if (currentIndex - 1 < 0) _songs.size - 1 else currentIndex - 1
        playCurrentSong()
    }

    fun getMediaPlayer(): MediaPlayer? = mediaPlayer

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}