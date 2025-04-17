package com.san.sanmusicplayerv_2.Model

import android.net.Uri

data class Album(
    val _id: Long,
    val title: String,
    val artist: String,
    val albumCover: Uri,
)
