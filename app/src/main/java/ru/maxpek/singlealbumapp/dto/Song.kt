package ru.maxpek.singlealbumapp.dto

data class Song(
    val id: Long,
    var reproduced: Boolean = false,
    val author: String,
    val file: String,
    val url: String,
    val timeSong: String) {


}