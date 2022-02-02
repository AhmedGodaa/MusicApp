package com.examplez.musicapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.VideoView
import com.examplez.musicapp.R
import com.examplez.musicapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySplashBinding
    var video: VideoView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playVideo()
        binding.button.setOnClickListener(View.OnClickListener {
            binding.videoView.stopPlayback()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })


    }


    private fun playVideo() {
        video!!.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.vid_splash));
        video!!.requestFocus()
        video!!.start()
        video!!.setOnCompletionListener { video!!.start() }
    }

    override fun onResume() {
        super.onResume()
        playVideo()

    }


}