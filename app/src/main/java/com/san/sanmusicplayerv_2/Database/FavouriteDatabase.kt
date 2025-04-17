package com.san.sanmusicplayerv_2.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.san.sanmusicplayerv_2.Model.FavouriteSongs
import com.san.sanmusicplayerv_2.dao.FavouriteSongDao

@Database(entities = [FavouriteSongs::class], version = 1, exportSchema = false)
abstract class FavouriteDatabase : RoomDatabase() {
    abstract fun favouriteSongDao(): FavouriteSongDao

    companion object {
        @Volatile private var instance: FavouriteDatabase? = null

        fun getDatabase(context: Context): FavouriteDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavouriteDatabase::class.java,
                    "favourites_db"
                ).build().also { instance = it }
            }
    }
}

