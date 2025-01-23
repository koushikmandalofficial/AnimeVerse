package com.sekho.animeverse.utils

import com.sekho.animeverse.api.AnimeDetailsResponse
import com.sekho.animeverse.api.TopAnimeResponse
import com.sekho.animeverse.model.ProducersResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeApiService {
    @GET("top/anime")
    suspend fun getTopAnime(): TopAnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetails(@Path("id") id: String): AnimeDetailsResponse

    @GET("anime/{id}")
    suspend fun getAnimeProducers(@Path("id") id: String): ProducersResponse
}
