@file:Suppress("DEPRECATION")

package com.sekho.animeverse.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.sekho.animeverse.R
import com.sekho.animeverse.adapter.OnboardingAdapter
import com.sekho.animeverse.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpScreen()
        setupIndicators()
        setCurrentIndicator(0)
        allClickListener()
        binding.viewPager.adapter = OnboardingAdapter(images)
    }

    private fun setUpScreen() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        val insetsController = WindowCompat.getInsetsController(window, findViewById(R.id.main))
        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false
    }

    private fun allClickListener() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                setCurrentIndicator(position)
                if (position == images.size - 1) {
                    binding.getStartedButton.visibility = View.VISIBLE
                    binding.getStartedButton.text = "Get Started ➨"
                }
                else {
                    binding.getStartedButton.text = "Next"
                }
            }
        })

        binding.getStartedButton.setOnClickListener {
            if (binding.viewPager.currentItem == images.size - 1) {
                val sharedPreferences = applicationContext.getSharedPreferences("AnimeVerse", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("onboarding_completed", true)
                editor.apply()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                binding.viewPager.currentItem += 1
            }
        }
    }

    private val images = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4
    )

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(images.size)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.indicator_inactive))
            indicators[i]?.layoutParams = layoutParams
            binding.indicatorLayout.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorLayout.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorLayout.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.indicator_active))
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.indicator_inactive))
            }
        }
    }

}