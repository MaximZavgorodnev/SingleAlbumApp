package ru.maxpek.singlealbumapp.repository

import ru.maxpek.singlealbumapp.dto.Executor

interface SongRepository {
    suspend fun getAlbum(): Executor
}