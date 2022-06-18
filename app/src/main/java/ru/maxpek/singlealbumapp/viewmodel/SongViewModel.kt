package ru.maxpek.singlealbumapp.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.maxpek.singlealbumapp.dto.ExecutorNew
import ru.maxpek.singlealbumapp.dto.Song
import ru.maxpek.singlealbumapp.model.FeedModelState
import ru.maxpek.singlealbumapp.repository.SongRepository
import ru.maxpek.singlealbumapp.repository.SongRepositoryImpl


class SongViewModel : ViewModel() {
    private val repository: SongRepository = SongRepositoryImpl()
    private val empty = Song(
        id = 0,
        reproduced = false,
        author = "",
        file = "",
        url = "",
        timeSong = "")


    val data: MutableLiveData<ExecutorNew> = repository.dataExecutorNew
    private val _currentSong = MutableLiveData(empty)
    val currentSong : LiveData<Song>
        get() = _currentSong
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState
    fun getAlbum() {
        viewModelScope.launch {
            try {
                repository.getAlbum()
            } catch (e: Exception) {
                println("ERORR")
            }
        }
    }

    fun onPlay(song: Song) {
        _dataState.value = FeedModelState(play = true)
        if (!songComparison(song)) {
            _currentSong.value = song
        }
        val tracks = data.value?.tracks.apply {
            this?.forEach {
                it.reproduced = it.id == song.id
            }
        }
        data.value = tracks?.let {
            data.value?.copy(
                tracks = it
            )
        }
    }

    fun onPause() {
        _dataState.value = FeedModelState(play = false)
        val tracks = data.value?.tracks.apply {
            this?.forEach {
                    it.reproduced = false
            }
        }
        data.value = tracks?.let {
            data.value?.copy(
                tracks = it
            )
        }
    }

    fun playerJob(): Boolean {
        return _currentSong.value?.id != 0L
    }

    fun songComparison(song: Song): Boolean {
        return _currentSong.value?.id == song.id
    }


}