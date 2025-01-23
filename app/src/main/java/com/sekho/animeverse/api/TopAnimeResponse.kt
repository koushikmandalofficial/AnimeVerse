package com.sekho.animeverse.api

import com.google.gson.annotations.SerializedName

data class TopAnimeResponse(
    @SerializedName("data") val data: List<AnimeData>
)

data class AnimeData(
    @SerializedName("mal_id") val malId: String,
    @SerializedName("title") val title: String,
    @SerializedName("trailer") val trailer: Trailer?,
    @SerializedName("episodes") val episodes: String?,
    @SerializedName("score") val score: String?,
    @SerializedName("images") val images: AnimeImages?,

    @SerializedName("synopsis") val synopsis: String,
    @SerializedName("rating") val rating: String,
    @SerializedName("rank") val rank: Int
)

data class Trailer(
    @SerializedName("images") val images: TrailerImages?
)

data class TrailerImages(
    @SerializedName("medium_image_url") val mediumImageUrl: String?
)

data class AnimeImages(
    @SerializedName("webp") val webp: WebpImages?
)

data class WebpImages(
    @SerializedName("image_url") val imageUrl: String?
)
