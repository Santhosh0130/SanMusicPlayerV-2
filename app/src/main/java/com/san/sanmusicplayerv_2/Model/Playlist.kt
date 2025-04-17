package com.san.sanmusicplayerv_2.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val songIds: List<Long> = listOf() // Store song IDs
)
