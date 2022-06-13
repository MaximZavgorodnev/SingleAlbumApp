package ru.maxpek.singlealbumapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import okhttp3.*
import ru.maxpek.singlealbumapp.dto.Executor
import ru.maxpek.singlealbumapp.dto.ExecutorNew
import ru.maxpek.singlealbumapp.dto.Song
import java.io.IOException


class SongRepositoryImpl : SongRepository {
    companion object{
        private const val ALBUM_URL = "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"
        private const val BASE_URL = "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
    }

    var executorNew: ExecutorNew? = null
    override val dataExecutorNew: MutableLiveData<ExecutorNew> =  MutableLiveData(executorNew)



    override suspend fun getAlbum(){

        var executor: Executor?
        val data = mutableListOf<Song>()
        val client = OkHttpClient()
        val gson = Gson()



        val request = Request.Builder()
            .url(ALBUM_URL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        executor = gson.fromJson(it.body?.string(), Executor::class.java)
                        if (executor != null) {
                            data.addAll(fromDto(executor!!))
                            executorNew = ExecutorNew(
                                executor!!.title,
                                executor!!.subtitle,
                                executor!!.artist,
                                executor!!.published,
                                executor!!.genre,
                                data
                            )
                            dataExecutorNew.postValue(executorNew)
                        }
                    }
                }
            }

        })


    }

    private fun fromDto(executor: Executor) = with(executor.tracks) {
        this.map {
            Song(it.id, false, executor.artist, it.file, BASE_URL+it.file, "4:24")
        }
    }


}


