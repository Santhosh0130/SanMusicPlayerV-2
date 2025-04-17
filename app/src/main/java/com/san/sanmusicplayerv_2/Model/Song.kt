package com.san.sanmusicplayerv_2.Model

import android.net.Uri

data class Song(
    val id: Long,
    val songCoverImage: Uri,
    val songName: String,
    val artistName: String,
    val songPath: String,
    val album: String,
)
