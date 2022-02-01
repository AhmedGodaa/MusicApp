package com.examplez.musicapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.examplez.musicapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.videoView!!.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.vid_splash));
        binding.videoView.start()
        binding.videoView.setOnClickListener {
            binding.videoView.stopPlayback();
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }


}