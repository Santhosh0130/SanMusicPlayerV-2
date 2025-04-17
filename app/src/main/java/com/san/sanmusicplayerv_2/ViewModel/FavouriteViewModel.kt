package com.san.sanmusicplayerv_2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.san.sanmusicplayerv_2.Database.FavouriteDatabase
import com.san.sanmusicplayerv_2.Model.FavouriteSongs
import com.san.sanmusicplayerv_2.Model.Song
import com.san.sanmusicplayerv_2.Repository.FavouriteRepository
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FavouriteRepository
//    val allFavourites: LiveData<List<Long>>

    init {
        val dao = FavouriteDatabase.getDatabase(application).favouriteSongDao()
        repository = FavouriteRepository(dao)
//        allFavourites = repository.getAllFavourites()
    }

    fun addToFavourites(songId: Long) {
        viewModelScope.launch {
            repository.addToFavourites(FavouriteSongs(songId))
        }
    }

    fun removeFromFavourites(songId: Long) {
        viewModelScope.launch {
            repository.removeFromFavourites(FavouriteSongs(songId))
        }
    }

//    fun isFavourite(songId: Long): LiveData<Boolean> {
//        return repository.isFavourite(songId)
//    }

    suspend fun isFavourite(songId: Long): Boolean {
        return repository.isFavourite(songId)
    }

    fun getAllFavourites(): LiveData<List<Long>> {
        return repository.getAllFavourites()
    }

}

