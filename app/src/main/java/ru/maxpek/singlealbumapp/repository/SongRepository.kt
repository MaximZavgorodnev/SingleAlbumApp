package ru.maxpek.singlealbumapp.repository

import ru.maxpek.singlealbumapp.dto.Executor
import ru.maxpek.singlealbumapp.dto.ExecutorNew

interface SongRepository {
    suspend fun getAlbum(): ExecutorNew
}