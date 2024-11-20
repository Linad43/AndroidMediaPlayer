package com.example.androidmediaplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.androidmediaplayer.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var mediaPlayer: MediaPlayer? = null
    private var numSong: Int = 0
    private var flagPlay = true
    private var songList = mutableListOf(
        R.raw.how_much_is_the_fish,
        R.raw.i_just_want_you,
        R.raw.rosenrot
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playSong()
        binding.previousFAB.setOnClickListener {
            if (numSong==0){
                numSong = songList.size-1
            }else{
                numSong--
            }

            playSong()
        }
        binding.nextFAB.setOnClickListener {
            if (numSong==songList.size-1){
                numSong = 0
            }else{
                numSong++
            }
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            flagPlay = true
            binding.playPauseFAB.setImageResource(R.drawable.play_fab)
            playSong()
        }
    }

    private fun playSong() {
        binding.playPauseFAB.setOnClickListener {
            if (flagPlay) {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(requireContext(), songList[numSong])
                    initializeSeekBar()
                }
                mediaPlayer?.start()
                flagPlay = false
                binding.playPauseFAB.setImageResource(R.drawable.pause_fab)
            }else{
                if(mediaPlayer!=null){
                    mediaPlayer?.pause()
                    flagPlay = true
                    binding.playPauseFAB.setImageResource(R.drawable.play_fab)
                }
            }
        }
        binding.stopFAB.setOnClickListener {
            if (mediaPlayer!=null){
                mediaPlayer?.stop()
                mediaPlayer?.reset()
                mediaPlayer?.release()
                flagPlay = true
                binding.playPauseFAB.setImageResource(R.drawable.play_fab)
                mediaPlayer = null
            }
        }
        binding.seekBar.setOnSeekBarChangeListener(@SuppressLint("AppCompatCustomView")
        object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun initializeSeekBar() {
        binding.seekBar.max = mediaPlayer!!.duration
        val handler = Handler()
        handler.postDelayed(object :Runnable{
            override fun run() {
                try {
                    binding.seekBar.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                }catch (e:Exception){
                    binding.seekBar.progress = 0
                }
            }

        },0)
    }
}