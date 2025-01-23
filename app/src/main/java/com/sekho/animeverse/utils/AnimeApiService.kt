package com.sekho.animeverse.utils

import com.sekho.animeverse.api.AnimeDetailsResponse
import com.sekho.animeverse.api.TopAnimeResponse
import com.sekho.animeverse.model.ProducersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeApiService {
    @GET("top/anime")
    fun getTopAnime(): Call<TopAnimeResponse>

    @GET("anime/{id}")
    fun getAnimeDetails(@Path("id") id: String): Call<AnimeDetailsResponse>

    @GET("anime/{id}")
    fun getAnimeProducers(@Path("id") id: String): Call<ProducersResponse>
}
