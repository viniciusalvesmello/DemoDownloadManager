package io.github.viniciusalvesmello.demodownloadmanager.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.github.viniciusalvesmello.demodownloadmanager.model.DownloadImage


class MainViewModel : ViewModel() {
    private val _data = MutableLiveData<MutableList<DownloadImage>>()
    val data: LiveData<MutableList<DownloadImage>>
        get() = _data

    init {
        _data.value = mutableListOf(
            DownloadImage(
                title = "Title imagem teste 1",
                description = "Description Imagem Teste 1",
                url = "https://upload.wikimedia.org/wikipedia/commons/d/de/Greeley_opportunity_5000.jpg"
            ),
            DownloadImage(
                title = "Title imagem teste 2",
                description = "Description Imagem Teste 2",
                url = "https://cdn-images-1.medium.com/max/1200/1*YK5PLgDKciZ0J66Ilvb9wQ.png"
            )
        )
    }
}
