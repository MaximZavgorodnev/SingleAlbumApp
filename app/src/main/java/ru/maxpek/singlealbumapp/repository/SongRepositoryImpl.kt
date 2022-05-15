package ru.maxpek.singlealbumapp.repository

import ru.maxpek.singlealbumapp.dto.Executor


class SongRepositoryImpl : SongRepository {
    companion object{
        private const val BASE_URL = "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
    }

    override suspend fun getAlbum(): Executor {
        TODO("Not yet implemented")
    }

}