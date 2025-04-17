package com.san.sanmusicplayerv_2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.*
import com.san.sanmusicplayerv_2.Model.FavouriteSongs
import com.san.sanmusicplayerv_2.Model.Playlist

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist)

    @Delete
    suspend fun deletePlaylist(playlist: Playlist)

    @Update
    suspend fun updatePlaylist(playlist: Playlist)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): LiveData<List<Playlist>>

}
