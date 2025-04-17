package com.san.sanmusicplayerv_2.Repository

import androidx.lifecycle.LiveData
import com.san.sanmusicplayerv_2.Model.FavouriteSongs
import com.san.sanmusicplayerv_2.dao.FavouriteSongDao

class FavouriteRepository(private val dao: FavouriteSongDao) {

    fun getAllFavourites(): LiveData<List<Long>> = dao.getAllFavourites()

    suspend fun addToFavourites(song: FavouriteSongs) {
        dao.addToFavourites(song)
    }

    suspend fun removeFromFavourites(song: FavouriteSongs) {
        dao.removeFromFavourites(song)
    }

//    fun isFavourite(songId: Long): LiveData<Boolean> = dao.isFavourite(songId)

    suspend fun isFavourite(songId: Long): Boolean {
        return dao.isFavourite(songId)
    }

}

