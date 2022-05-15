package ru.maxpek.singlealbumapp.dto

data class Track (
    val id: Long,
    var reproduced: Boolean = false,
    val author: String,
    val file: String,
    val timeSong: String
)

