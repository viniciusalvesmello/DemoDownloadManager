package io.github.viniciusalvesmello.demodownloadmanager.model

data class DownloadImage(
    var id : Long = -1L,
    val title: String,
    val description: String,
    val url: String
)