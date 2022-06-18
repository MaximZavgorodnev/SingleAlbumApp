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
        val binding = ActivityMainBinding.inflate(layoutInflater)


        val adapter = SongAdapter (object : AdapterCallback {
            override fun onPlay(song: Song) {
                if (!viewModel.songComparison(song)) {
                    mediaObserver.player!!.reset()
                    mediaObserver.apply {
                        player?.setDataSource(
                            song.url
                        )
                    }.play()
                    viewModel.onPlay(song)
                } else {
                        mediaObserver.player!!.start()
                        viewModel.onPlay(song)
                }
            }
            override fun onPause() {
                mediaObserver.onStateChanged(this@MainActivity, Lifecycle.Event.ON_PAUSE)
                viewModel.onPause()
            }
        })


        viewModel.getAlbum()
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.list.adapter = adapter
        viewModel.data.observe(this@MainActivity){
            if (it != null){
                binding.progress.visibility = View.GONE
                binding.nameAlbum.text = it.title
                binding.nameActor.text = it.artist
                binding.published.text = it.published
                binding.genre.text = it.genre
                adapter.submitList(it.tracks)
            }

        }
        viewModel.dataState.observe(this@MainActivity){
            binding.play.isChecked = it.play
        }
        binding.play.setOnClickListener {
            val song = if (!viewModel.playerJob()) {
                viewModel.data.value!!.tracks[0]
            } else {
                viewModel.currentSong.value!!
            }
            if (binding.play.isChecked) {
                if (!viewModel.songComparison(song)) {
                    mediaObserver.player!!.reset()
                    mediaObserver.apply {
                        player?.setDataSource(
                            song.url
                        )
                    }.play()
                    viewModel.onPlay(song)
                } else {
                    mediaObserver.player!!.start()
                    viewModel.onPlay(song)
                }
            } else {
                mediaObserver.onStateChanged(this@MainActivity, Lifecycle.Event.ON_PAUSE)
                viewModel.onPause()
            }

        }

        binding.last.setOnClickListener {
            if (viewModel.playerJob()) {
                val size = viewModel.data.value!!.tracks.size
                val lastId = viewModel.currentSong.value!!.id - 2
                val song = if (lastId < 0) viewModel.data.value!!.tracks[size-1] else viewModel.data.value!!.tracks[lastId.toInt()]
                mediaObserver.player!!.reset()
                mediaObserver.apply {
                    player?.setDataSource(
                        song.url
                    )
                }.play()
                viewModel.onPlay(song)
            }
        }

        binding.next.setOnClickListener {
            if (viewModel.playerJob()) {
                val size = viewModel.data.value!!.tracks.size
                val nextId = viewModel.currentSong.value!!.id
                val song = if (nextId < size) viewModel.data.value!!.tracks[nextId.toInt()] else viewModel.data.value!!.tracks[0]
                mediaObserver.player!!.reset()
                mediaObserver.apply {
                    player?.setDataSource(
                        song.url
                    )
                }.play()
                viewModel.onPlay(song)
            }
        }




    }


}