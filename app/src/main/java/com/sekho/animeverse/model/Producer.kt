package com.sekho.animeverse.model

import com.google.gson.annotations.SerializedName


data class Producer(
    @SerializedName("mal_id") val malId: String,
    @SerializedName("name") val name: String
)

data class ProducerData(
    @SerializedName("producers") val producers: List<Producer>
)

data class ProducersResponse(
    @SerializedName("data") val data: ProducerData
)
