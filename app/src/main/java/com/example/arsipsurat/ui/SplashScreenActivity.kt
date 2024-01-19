package com.example.arsipsurat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import com.example.arsipsurat.databinding.ActivitySplashScreenBinding
import com.example.arsipsurat.ui.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch {
            delay(3.seconds)
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            finish()
        }

        supportActionBar?.hide()
    }
}