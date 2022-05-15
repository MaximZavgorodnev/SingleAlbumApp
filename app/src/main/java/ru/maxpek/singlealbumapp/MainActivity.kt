package ru.maxpek.singlealbumapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

import ru.maxpek.singlealbumapp.databinding.ActivityMainBinding
import ru.maxpek.singlealbumapp.dto.ExecutorNew
import ru.maxpek.singlealbumapp.viewmodel.SongViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: SongViewModel by viewModels()
        viewModel.getAlbum()
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = viewModel.data.value
        binding.album.text = data?.title

//        val job = CoroutineScope.runCatching {  }
//
//

//        coroutineScope {
//            async {
//
//                        try {
//                            data.value = repository.getAlbum()
//                        } catch (e: Exception) { }
//                    }
//                }
//
//
    }
}