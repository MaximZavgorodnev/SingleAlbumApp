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
        var idOfPlayedTrack = 0L
        lifecycle.addObserver(mediaObserver)
        val binding = ActivityMainBinding.inflate(layoutInflater)


        val adapter = SongAdapter (object : AdapterCallback {
            override fun onPlay(song: Song) {
                if (song.id != idOfPlayedTrack) {
                    mediaObserver.player!!.reset()
                    mediaObserver.apply {
                        player?.setDataSource(
                            song.url
                        )
                    }.play()
                    idOfPlayedTrack = song.id
                    viewModel.onPlay(song)
                    binding.play.isChecked = true
                } else {
                    if (!song.reproduced) {
                        mediaObserver.player!!.start()
                        viewModel.onPlay(song)
                        binding.play.isChecked = true
                    } else {
                        mediaObserver.onStateChanged(this@MainActivity, Lifecycle.Event.ON_PAUSE)
                        viewModel.onPlay(song)
                        binding.play.isChecked = false
                    }
                }
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
        binding.play.setOnClickListener {
            val song = viewModel.data.value!!.tracks[idOfPlayedTrack.toInt()]

            if (!binding.play.isChecked){
                mediaObserver.onStateChanged(this@MainActivity, Lifecycle.Event.ON_PAUSE)
                viewModel.onPlay(song)
                binding.play.isChecked = false
            } else {
                if (song.reproduced) {
                    mediaObserver.player!!.start()
                    viewModel.onPlay(song)
                    binding.play.isChecked = true
                } else {
                    mediaObserver.apply {
                        player?.setDataSource(
                            song.url
                        )
                    }.play()
                    viewModel.onPlay(song)
                    binding.play.isChecked = true
                }
            }

        }

        binding.last.setOnClickListener {

        }

        binding.next.setOnClickListener {

        }




    }


}