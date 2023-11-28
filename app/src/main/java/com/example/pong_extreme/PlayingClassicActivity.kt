package com.example.pong_extreme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong_extreme.databinding.ActivityPlayingClassicBinding

class PlayingClassicActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayingClassicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayingClassicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEndGame.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
        }
    }
}