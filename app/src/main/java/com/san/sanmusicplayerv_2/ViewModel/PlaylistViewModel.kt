package com.san.sanmusicplayerv_2.ViewModel

import android.app.Application
import androidx.lifecycle.*
import com.san.sanmusicplayerv_2.Database.AppDatabase
import com.san.sanmusicplayerv_2.Model.Playlist
import com.san.sanmusicplayerv_2.Repository.PlaylistRepository
import kotlinx.coroutines.*

class PlaylistViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PlaylistRepository
    val allPlaylists: LiveData<List<Playlist>>

    init {
        val dao = AppDatabase.getDatabase(application).playlistDao()
        repository = PlaylistRepository(dao)
        allPlaylists = repository.allPlaylists
    }

    fun insertPlaylist(playlist: Playlist) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(playlist)
    }

    fun deletePlaylist(playlist: Playlist) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(playlist)
    }

    fun updatePlaylist(playlist: Playlist) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(playlist)
    }

}