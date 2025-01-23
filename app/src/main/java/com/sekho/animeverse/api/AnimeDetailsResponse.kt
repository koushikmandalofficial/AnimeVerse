package com.sekho.animeverse.api

import com.google.gson.annotations.SerializedName

data class AnimeDetailsResponse(
    @SerializedName("data") val data: AnimeDetailsData
)

data class AnimeDetailsData(
    @SerializedName("mal_id") val malId: String,
    @SerializedName("title") val title: String,
    @SerializedName("trailer") val trailer: AnimeDetailsTrailer?,
    @SerializedName("episodes") val episodes: String?,
    @SerializedName("score") val score: String?,
    @SerializedName("images") val images: AnimeDetailsAnimeImages?,
    @SerializedName("synopsis") val synopsis: String,
    @SerializedName("rating") val rating: String,
    @SerializedName("rank") val rank: Int
)

data class AnimeDetailsTrailer(
    @SerializedName("youtube_id") val youtubeId: String?,
    @SerializedName("images") val images: AnimeDetailsTrailerImages?
)


data class AnimeDetailsTrailerImages(
    @SerializedName("medium_image_url") val mediumImageUrl: String?
)

data class AnimeDetailsAnimeImages(
    @SerializedName("webp") val webp: AnimeDetailsWebpImages?
)

data class AnimeDetailsWebpImages(
    @SerializedName("image_url") val imageUrl: String?
)
