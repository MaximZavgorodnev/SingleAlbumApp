package ru.maxpek.singlealbumapp.dto

data class Executor(
    val title: String,
    val artist: String,
    val published: String,
    val genre: String,
    val tracks: List<Song>
)
