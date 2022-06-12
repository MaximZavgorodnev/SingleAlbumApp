package ru.maxpek.singlealbumapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

import ru.maxpek.singlealbumapp.databinding.ActivityMainBinding
import ru.maxpek.singlealbumapp.dto.ExecutorNew
import ru.maxpek.singlealbumapp.viewmodel.SongViewModel


class MainActivity : AppCompatActivity() {
    private val mediaObserver = MediaLifecycleObserver()
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: SongViewModel by viewModels()



        viewModel.getAlbum()
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val data = viewModel.data.value
        viewModel.data.observeForever {
            if (it != null){
                binding.progress.visibility = View.GONE
                binding.album.text = it.title
            }

        }
//        binding.album.text = data?.title

        binding.play.setOnClickListener {
            mediaObserver.apply {
                resources.openRawResourceFd(R.raw.ring).use { afd ->
                    player?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                }
            }.play()
        }

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