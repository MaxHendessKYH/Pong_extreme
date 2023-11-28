package com.example.pong_extreme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pong_extreme.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

 lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClassic.setOnClickListener {
            val intent = Intent(this, PlayingClassicActivity::class.java)
        }

        binding.btnTimed.setOnClickListener {
            val intent = Intent(this, PlayingTimedActivity::class.java)
        }


    }
}