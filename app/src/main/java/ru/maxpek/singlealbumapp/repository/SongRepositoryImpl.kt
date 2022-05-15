package ru.maxpek.singlealbumapp.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.maxpek.singlealbumapp.dto.Executor
import ru.maxpek.singlealbumapp.dto.ExecutorNew
import ru.maxpek.singlealbumapp.dto.Song


class SongRepositoryImpl : SongRepository {
    companion object{
        private const val ALBUM_URL = "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"
        private const val BASE_URL = "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
    }


    override suspend fun getAlbum(): ExecutorNew {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(ALBUM_URL)
            .build()

        var executor: Executor? = null
        val data = mutableListOf<Song>()
        try {
            coroutineScope {
                async {
                    val response = client.newCall(request).execute()
                    executor = (response.body?.string()) as Executor
                }
            }.await()
        } catch (e: Exception) {
        }

        if (executor != null) {
            data.addAll(fromDto(executor!!))
        }
        return ExecutorNew(
            executor!!.title,
            executor!!.artist,
            executor!!.published,
            executor!!.genre,
            data
        )
    }

    private fun fromDto(executor: Executor) = with(executor.tracks) {
        this.map {
            Song(it.id, false, executor.artist, it.file, BASE_URL+it.file, "4:24")
        }
    }

}