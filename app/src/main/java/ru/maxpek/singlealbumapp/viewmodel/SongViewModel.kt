package ru.maxpek.singlealbumapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.maxpek.singlealbumapp.MediaLifecycleObserver
import ru.maxpek.singlealbumapp.dto.ExecutorNew
import ru.maxpek.singlealbumapp.repository.SongRepository
import ru.maxpek.singlealbumapp.repository.SongRepositoryImpl
import java.util.concurrent.Flow
import javax.inject.Inject



class SongViewModel : ViewModel() {
    private val repository: SongRepository = SongRepositoryImpl()

    val data: MutableLiveData<ExecutorNew> = repository.dataExecutorNew
//    val data1: kotlinx.coroutines.flow.Flow<ExecutorNew> = ExecutorNew()


    fun getAlbum() {
        viewModelScope.launch {
            try {
                repository.getAlbum()
            } catch (e: Exception) {
                println("ERORR")
            }
        }
    }

    fun onPlay(){

    }


}