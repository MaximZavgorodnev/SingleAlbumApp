package ru.maxpek.singlealbumapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.maxpek.singlealbumapp.adapter.AdapterCallback
import ru.maxpek.singlealbumapp.adapter.SongAdapter

import ru.maxpek.singlealbumapp.databinding.ActivityMainBinding
import ru.maxpek.singlealbumapp.dto.ExecutorNew
import ru.maxpek.singlealbumapp.dto.Song
import ru.maxpek.singlealbumapp.viewmodel.SongViewModel


class MainActivity : AppCompatActivity() {
    private val mediaObserver = MediaLifecycleObserver()
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: SongViewModel by viewModels()
        lifecycle.addObserver(mediaObserver)


        val adapter = SongAdapter (object : AdapterCallback {
            override fun onPlay(song: Song) {
                if (mediaObserver.player?.isPlaying!!){
                    mediaObserver.player!!.reset()
                }
                if (!song.reproduced) {
                    mediaObserver.apply {
                        player?.setDataSource(
                            song.url
                        )
                    }.play()
                    viewModel.onPlay(song)
                } else {
                    mediaObserver.onStateChanged(this@MainActivity, Lifecycle.Event.ON_PAUSE)
                }
            }
        })


        viewModel.getAlbum()
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val data = viewModel.data.value
        binding.list.adapter = adapter
        viewModel.data.observeForever{
            if (it != null){
                binding.progress.visibility = View.GONE
                binding.nameAlbum.text = it.title
                binding.nameActor.text = it.artist
                binding.published.text = it.published
                binding.genre.text = it.genre
                val track = adapter.itemCount < it.tracks.size
                adapter.submitList(it.tracks) {
                    if (track) {
                        binding.list.smoothScrollToPosition(0)
                    }
                }
            }

        }
        binding.play.setOnClickListener {
            mediaObserver.apply {
                resources.openRawResourceFd(R.raw.ring).use { afd ->
                    player?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                }
            }.play()
        }


    }
}