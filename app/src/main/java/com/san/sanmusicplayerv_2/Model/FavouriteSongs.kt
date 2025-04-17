package com.san.sanmusicplayerv_2.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_songs")
data class FavouriteSongs(
    @PrimaryKey val songId: Long,
)
