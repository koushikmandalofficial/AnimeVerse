package com.sekho.animeverse.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.sekho.animeverse.R
import com.sekho.animeverse.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpScreen()
        nextScreen()
    }

    private fun nextScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreferences = applicationContext.getSharedPreferences("AnimeVerse", MODE_PRIVATE)
            val onboardingCompleted = sharedPreferences.getBoolean("onboarding_completed", false)

            val intent: Intent = when {
                onboardingCompleted -> {
                    Intent(this, HomeActivity::class.java)
                }
                else -> Intent(this, MainActivity::class.java)
            }
            startActivity(intent)
            finish()

        }, 1000)
    }

    private fun setUpScreen(){
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        WindowCompat.getInsetsController(window,findViewById(R.id.main)).isAppearanceLightStatusBars = false
    }

}