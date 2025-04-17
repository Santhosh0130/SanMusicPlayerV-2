package com.san.sanmusicplayerv_2.ViewModel

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import com.san.sanmusicplayerv_2.Model.Album

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    fun fetchAlbums(context: Context): List<Album> {
        val albumList = mutableListOf<Album>()

        val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.ALBUM_ART
        )

        val cursor = context.contentResolver.query(
            uri, projection, null, null,
            "${MediaStore.Audio.Albums.ALBUM} ASC"
        )

        cursor?.use {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID)
            val albumCol = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM)
            val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST)

            while (it.moveToNext()) {
                val id = it.getLong(idCol)
                val name = it.getString(albumCol)
                val artist = it.getString(artistCol)
                val artUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"), id
                )

                albumList.add(Album(id, name, artist, artUri))
            }
        }

        return albumList
    }

}


