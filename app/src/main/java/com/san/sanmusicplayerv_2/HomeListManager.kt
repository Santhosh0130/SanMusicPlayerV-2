package com.san.sanmusicplayerv_2

import android.content.Context

class HomeListManager(context: Context) {

    private val recentPrefs = context.getSharedPreferences("recently_played", Context.MODE_PRIVATE)

    private val mostPrefs = context.getSharedPreferences("most_played", Context.MODE_PRIVATE)

    fun addSong(songId: Long) {
        val ids = getRecentlyPlayed().toMutableList()
        ids.remove(songId) // Avoid duplicates
        ids.add(0, songId) // Add to top
        if (ids.size > 10) ids.removeAt(ids.size - 1) // Keep list size small

        recentPrefs.edit().putString("ids", ids.joinToString(",")).apply()
    }

    fun getRecentlyPlayed(): List<Long> {
        val saved = recentPrefs.getString("ids", "") ?: ""
        return if (saved.isNotEmpty())
            saved.split(",").mapNotNull { it.toLongOrNull() }
        else
            emptyList()
    }

    fun increasePlayCount(songId: Long) {
        val count = mostPrefs.getInt(songId.toString(), 0)
        mostPrefs.edit().putInt(songId.toString(), count + 1).apply()
    }

    fun getMostPlayed(): List<Pair<Long, Int>> {
        return mostPrefs.all
            .mapNotNull { entry ->
                val id = entry.key.toLongOrNull()
                val count = entry.value as? Int
                if (id != null && count != null) Pair(id, count)
                else null
            }
            .sortedByDescending { it.second }
    }
}
