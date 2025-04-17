package com.san.sanmusicplayerv_2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.san.sanmusicplayerv_2.Model.FavouriteSongs

@Dao
interface FavouriteSongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavourites(song: FavouriteSongs)

    @Delete
    suspend fun removeFromFavourites(song: FavouriteSongs)

    @Query("SELECT songId FROM favourite_songs")
    fun getAllFavourites(): LiveData<List<Long>>

//    @Query("SELECT EXISTS(SELECT 1 FROM favourite_songs WHERE songId = :id)")
//    fun isFavourite(id: Long): LiveData<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_songs WHERE songId = :songId)")
    suspend fun isFavourite(songId: Long): Boolean

}
