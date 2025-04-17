package com.san.sanmusicplayerv_2.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.san.sanmusicplayerv_2.Converters
import com.san.sanmusicplayerv_2.dao.FavouriteSongDao
import com.san.sanmusicplayerv_2.Model.FavouriteSongs
import com.san.sanmusicplayerv_2.Model.Playlist
import com.san.sanmusicplayerv_2.dao.PlaylistDao

@Database(entities = [Playlist::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "music_player_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
