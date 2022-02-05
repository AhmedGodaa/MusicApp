package com.examplez.musicapp.activities

import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.examplez.musicapp.R
import com.examplez.musicapp.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playVideo()
        val logoAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_logo)
        binding.logo.animation = logoAnimation


    }
    override fun onResume() {
        super.onResume()
        playVideo()

    }

    private fun playVideo() {
        val uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.vid_splash)
        binding.videoView.setVideoURI(uri)
        binding.videoView.start()
        binding.videoView.setOnCompletionListener { binding.videoView.start() }
    }


}