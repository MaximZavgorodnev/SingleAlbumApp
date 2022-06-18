package ru.maxpek.singlealbumapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.SeekBar
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
        mediaObserver.player?.setOnCompletionListener {
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



        val adapter = SongAdapter (object : AdapterCallback {
            override fun onPlay(song: Song) {
                if (!viewModel.songComparison(song)) {
                    mediaObserver.onStateChanged(this@MainActivity, Lifecycle.Event.ON_STOP)
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



        binding.list.adapter = adapter
        viewModel.data.observe(this@MainActivity){
            if (it != null){
                adapter.submitList(it.tracks)
                binding.progress.visibility = View.GONE
                binding.nameAlbum.text = it.title
                binding.nameActor.text = it.artist
                binding.published.text = it.published
                binding.genre.text = it.genre
            }

        }
        viewModel.dataState.observe(this@MainActivity){
            binding.play.isChecked = it.play
            binding.overStart.visibility = View.VISIBLE
            binding.timeStart.visibility = View.VISIBLE
            binding.positionBar.max = mediaObserver.whatTotalTime()
        }
        binding.play.setOnClickListener {
            val song = if (!viewModel.playerJob()) {
                viewModel.data.value!!.tracks[0]
            } else {
                viewModel.currentSong.value!!
            }
            if (binding.play.isChecked) {
                if (!viewModel.songComparison(song)) {
                    mediaObserver.onStateChanged(this@MainActivity, Lifecycle.Event.ON_STOP)
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

        binding.stop.setOnClickListener {
            mediaObserver.onStateChanged(this@MainActivity, Lifecycle.Event.ON_STOP)
            viewModel.onStop()
            binding.overStart.visibility = View.GONE
            binding.timeStart.visibility = View.GONE
        }
        setContentView(binding.root)


        binding.positionBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mediaObserver.player?.seekTo(progress)
                    }
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {
                }
                override fun onStopTrackingTouch(p0: SeekBar?) {
                }
            }
        )

        @SuppressLint("HandlerLeak")
        var handler = object : Handler() {
            @SuppressLint("SetTextI18n")
            override fun handleMessage(msg: Message) {
                var currentPosition = msg.what

                // Update positionBar
                binding.positionBar.progress = currentPosition
                // Update Labels
                var elapsedTime = createTimeLabel(currentPosition)
                binding.timeStart.text = elapsedTime

                var remainingTime = createTimeLabel(mediaObserver.whatTotalTime() - currentPosition)
                binding.overStart.text = "-$remainingTime"
            }
        }

        // Thread
        Thread(Runnable {
            while (mediaObserver.player != null) {
                try {
                    var msg = Message()
                    msg.what = mediaObserver.player!!.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()






    }



    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }


}