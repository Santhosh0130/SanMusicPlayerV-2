package com.san.sanmusicplayerv_2.Repository

import androidx.lifecycle.LiveData
import com.san.sanmusicplayerv_2.Model.Playlist
import com.san.sanmusicplayerv_2.dao.PlaylistDao

class PlaylistRepository(private val dao: PlaylistDao) {
    val allPlaylists: LiveData<List<Playlist>> = dao.getAllPlaylists()

    suspend fun insert(playlist: Playlist) = dao.insertPlaylist(playlist)
    suspend fun delete(playlist: Playlist) = dao.deletePlaylist(playlist)
    suspend fun update(playlist: Playlist) = dao.updatePlaylist(playlist)
}