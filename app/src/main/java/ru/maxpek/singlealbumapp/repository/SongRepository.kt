package ru.maxpek.singlealbumapp.repository

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.singlealbumapp.dto.ExecutorNew

interface SongRepository {
    val dataExecutorNew: MutableLiveData<ExecutorNew>
    suspend fun getAlbum()

}