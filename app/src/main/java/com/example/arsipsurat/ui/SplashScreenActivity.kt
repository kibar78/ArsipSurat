package com.example.arsipsurat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.arsipsurat.databinding.ActivitySplashScreenBinding
import com.example.arsipsurat.ui.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler().postDelayed({
            startActivity(Intent( this, LoginActivity::class.java))
            finish()
        }, 3000)

        supportActionBar?.hide()
    }
}