package com.example.pong_extreme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat

class PlayingClassicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // It makes transparent status bar and navigation bar
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // It will hide the title bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_playing_classic)
    }
}