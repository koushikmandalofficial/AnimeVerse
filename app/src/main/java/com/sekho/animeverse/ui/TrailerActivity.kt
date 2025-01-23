package com.sekho.animeverse.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.sekho.animeverse.R
import com.sekho.animeverse.adapter.AnimeAdapter
import com.sekho.animeverse.adapter.ProducersAdapter
import com.sekho.animeverse.api.AnimeDetailsResponse
import com.sekho.animeverse.api.TopAnimeResponse
import com.sekho.animeverse.databinding.ActivityTrailerBinding
import com.sekho.animeverse.model.AnimeModel
import com.sekho.animeverse.model.ProducersResponse
import com.sekho.animeverse.utils.AnimeApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrailerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrailerBinding
    var contentId: String = ""
    var isMuted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpScreen()
        allclicklistener()
        contentId = intent.getStringExtra("contentId").toString()

        val retrofit = Retrofit.Builder().baseUrl("https://api.jikan.moe/v4/").addConverterFactory(GsonConverterFactory.create()).build()

        val animeApiService = retrofit.create(AnimeApiService::class.java)
        fetchSimilarData(animeApiService)
        fetchDetailsData(animeApiService)
        fetchProducersData(animeApiService)

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

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.watchNowBtn.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playTrailer(videoId: String){
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
                binding.youtubePlayerView.visibility = View.VISIBLE
                binding.muteUnmuteImg.visibility = View.VISIBLE
                binding.muteUnmuteImg.setOnClickListener {
                    if (isMuted) {
                        youTubePlayer.unMute()
                        binding.muteUnmuteImg.setImageResource(R.drawable.baseline_volume_up_24)
                        isMuted = false
                    }
                    else {
                        youTubePlayer.mute()
                        binding.muteUnmuteImg.setImageResource(R.drawable.baseline_volume_mute_24)
                        isMuted = true
                    }

                }
            }

            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                if (state == PlayerConstants.PlayerState.ENDED) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }
        })
    }

    private fun setUpScreen(){
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
        val insetsController = WindowCompat.getInsetsController(window, findViewById(R.id.main))
        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false
    }


    private fun fetchSimilarData(animeApiService: AnimeApiService) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    animeApiService.getTopAnime()
                }

                val animes: MutableList<AnimeModel> = mutableListOf()

                response.data.forEachIndexed { index, anime ->
                    val id = anime.malId
                    val contName = anime.title
                    val episodes = anime.episodes ?: ""
                    val score = anime.score ?: ""
                    val posterImage = anime.images?.webp?.imageUrl ?: ""

                    val animeModel = AnimeModel(id, contName, posterImage, score, episodes)

                    if (id != contentId) {
                        animes.add(animeModel)
                    }
                }

                val vodShowMoreAdapter = AnimeAdapter(this@TrailerActivity, animes)
                binding.similarRecyclerView.adapter = vodShowMoreAdapter
                binding.similarRecyclerView.layoutManager = GridLayoutManager(this@TrailerActivity, 2)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchDetailsData(animeApiService: AnimeApiService) {
        val call = animeApiService.getAnimeDetails(contentId)

        call.enqueue(object : Callback<AnimeDetailsResponse> {
            override fun onResponse(
                call: Call<AnimeDetailsResponse>,
                response: retrofit2.Response<AnimeDetailsResponse>
            ) {
                if (response.isSuccessful) {
                    val animeDetails = response.body()?.data

                    animeDetails?.let { details ->
                        val title = details.title
                        val bannerUrl = details.trailer?.images?.mediumImageUrl
                        val episodes = details.episodes ?: "N/A"
                        val score = details.score ?: "N/A"
                        val synopsis = details.synopsis
                        val rating = details.rating
                        val rank = details.rank
                        val youtubeId = details.trailer?.youtubeId

                        binding.metaDataLayout.visibility = View.VISIBLE

                        binding.trailerTitle.text = title
                        binding.trailerDescription.text = synopsis
                        binding.episodesText.text = "$episodes Episodes"
                        binding.scoreTxt.text = "  ⭐  $score "
                        binding.ratingTxt.text = "$rating"
                        binding.rankingTxt.text = " # $rank "

                        Glide.with(this@TrailerActivity).load(bannerUrl).error(R.drawable.land_load).placeholder(R.drawable.land_load).into(binding.bannerImg)

                        playTrailer(youtubeId.toString())
                    }
                }
            }

            override fun onFailure(call: Call<AnimeDetailsResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun fetchProducersData(animeApiService: AnimeApiService) {
        val call = animeApiService.getAnimeProducers(contentId)

        call.enqueue(object : Callback<ProducersResponse> {
            override fun onResponse(
                call: Call<ProducersResponse>,
                response: retrofit2.Response<ProducersResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("TrailerActivityResponse", response.body().toString())
                    val producers = response.body()?.data?.producers

                    if (!producers.isNullOrEmpty()) {
                        val adapter = ProducersAdapter(producers)
                        binding.producersRecyclerView.layoutManager = LinearLayoutManager(this@TrailerActivity, LinearLayoutManager.HORIZONTAL, false)
                        binding.producersRecyclerView.adapter = adapter
                    } else {
                        Log.e("Error", "No producers found")
                    }
                }
            }

            override fun onFailure(call: Call<ProducersResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("TrailerActivityResponse", "Error fetching producers data: ${t.message}")
            }
        })
    }

    public override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
    }



}