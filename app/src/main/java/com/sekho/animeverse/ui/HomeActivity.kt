@file:Suppress("DEPRECATION")

package com.sekho.animeverse.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.sekho.animeverse.R
import com.sekho.animeverse.adapter.AnimeAdapter
import com.sekho.animeverse.adapter.BannerAdapter
import com.sekho.animeverse.databinding.ActivityHomeBinding
import com.sekho.animeverse.model.AnimeModel
import com.sekho.animeverse.model.BannerModel
import com.sekho.animeverse.utils.AnimeApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Timer
import java.util.TimerTask

class HomeActivity : AppCompatActivity() {

    private val activityScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var binding: ActivityHomeBinding
    private var swipeTimer: Timer? = null
    private lateinit var indicators: Array<ImageView?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpScreen()
        allclicklistener()

        val retrofit = Retrofit.Builder().baseUrl("https://api.jikan.moe/v4/").addConverterFactory(GsonConverterFactory.create()).build()
        val animeApiService = retrofit.create(AnimeApiService::class.java)
        fetchBannerData(animeApiService)

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData(animeApiService)
            binding.nestedScrollView.scrollTo(0, 0)
            binding.swipeRefreshLayout.isRefreshing = false
        }

    }

    private fun allclicklistener() {
        binding.searchBtn.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
        binding.wishlistBtn.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
        binding.searchBtn.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshData(animeApiService: AnimeApiService) {
        binding.indicatorLayout.removeAllViews()
        fetchBannerData(animeApiService)
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

    private fun fetchBannerData(animeApiService:AnimeApiService) {
        activityScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    animeApiService.getTopAnime()
                }

                binding.shimmerForViewpager.visibility = View.GONE
                binding.shimmerViewContainerHomeRecyclerviewLayout.visibility = View.GONE
                binding.recycleViewLayout.visibility = View.VISIBLE

                val banners: MutableList<BannerModel> = mutableListOf()
                val animes: MutableList<AnimeModel> = mutableListOf()

                response.data?.forEachIndexed { index, anime ->
                    val id = anime.malId
                    val contName = anime.title
                    val banLink = anime.trailer?.images?.mediumImageUrl ?: ""
                    val episodes = anime.episodes ?: ""
                    val score = anime.score ?: ""
                    val posterImage = anime.images?.webp?.imageUrl ?: ""

                    val banner = BannerModel(id, contName, banLink, score, episodes)
                    val animeModel = AnimeModel(id, contName, posterImage, score, episodes)

                    if (index in 0..6) {
                        banners.add(banner)
                    }

                    animes.add(animeModel)
                }

                val vodShowMoreAdapter = AnimeAdapter(this@HomeActivity, animes)
                binding.recyclerView.adapter = vodShowMoreAdapter
                binding.recyclerView.layoutManager = GridLayoutManager(this@HomeActivity, 2)

                val firstItem = banners.firstOrNull()?.copy()
                val lastItem = banners.lastOrNull()?.copy()

                if (firstItem != null && lastItem != null) {
                    banners.add(0, lastItem)
                    banners.add(firstItem)
                }

                val adapter = BannerAdapter(this@HomeActivity, banners)
                binding.viewPager.adapter = adapter
                binding.viewPager.setCurrentItem(1, false)

                setupIndicators(banners.size - 2)
                setCurrentIndicator(0)

                val handler = Handler(Looper.getMainLooper())
                val update = object : Runnable {
                    override fun run() {
                        val nextItem = binding.viewPager.currentItem + 1
                        binding.viewPager.setCurrentItem(nextItem, true)
                    }
                }

                swipeTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        handler.post(update)
                    }
                }, 4000, 4000)

                binding.viewPager.registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        val index = when (position) {
                            0 -> banners.size - 3
                            banners.size - 1 -> 0
                            else -> position - 1
                        }
                        setCurrentIndicator(index)
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                        if (state == ViewPager2.SCROLL_STATE_IDLE) {
                            when (binding.viewPager.currentItem) {
                                0 -> binding.viewPager.setCurrentItem(banners.size - 2, false)
                                banners.size - 1 -> binding.viewPager.setCurrentItem(1, false)
                            }
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun setCurrentIndicator(index: Int) {
        for (i in indicators.indices) {
            val drawableId = if (i == index) R.drawable.indicator_active else R.drawable.indicator_inactive
            indicators[i]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, drawableId))
        }
    }

    private fun setupIndicators(count: Int) {
        indicators = arrayOfNulls(count)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.indicator_inactive))
            indicators[i]?.layoutParams = layoutParams
            binding.indicatorLayout.addView(indicators[i])
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }


}