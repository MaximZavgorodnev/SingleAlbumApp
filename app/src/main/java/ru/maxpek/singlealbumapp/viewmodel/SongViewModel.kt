package ru.maxpek.singlealbumapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.maxpek.singlealbumapp.dto.ExecutorNew
import ru.maxpek.singlealbumapp.repository.SongRepository
import ru.maxpek.singlealbumapp.repository.SongRepositoryImpl
import javax.inject.Inject



class SongViewModel : ViewModel() {
    private val repository: SongRepository = SongRepositoryImpl()

    val data = MutableLiveData<ExecutorNew>()

    fun getAlbum() {
        viewModelScope.launch {
            try {
                data.value = repository.getAlbum()
            } catch (e: Exception) { }
        }
    }


}