package com.stepstonks.app.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.stepstonks.app.domain.model.CryptoAsset

data class CoinDto(
    @SerializedName("id") val id: String = "",
    @SerializedName("symbol") val symbol: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("image") val image: String = "",
    @SerializedName("current_price") val currentPrice: Double = 0.0,
    @SerializedName("market_cap") val marketCap: Double = 0.0,
    @SerializedName("market_cap_rank") val marketCapRank: Int = 999,
    @SerializedName("price_change_percentage_24h") val priceChange24h: Double = 0.0,
    @SerializedName("sparkline_in_7d") val sparkline: SparklineDto? = null
)

data class SparklineDto(
    @SerializedName("price") val price: List<Double> = emptyList()
)

fun CoinDto.toDomain() = CryptoAsset(
    id = id,
    symbol = symbol.uppercase(),
    name = name,
    currentPrice = currentPrice,
    priceChange24h = priceChange24h,
    marketCap = marketCap,
    marketCapRank = marketCapRank,
    sparkline = sparkline?.price?.takeLast(48) ?: emptyList(),
    imageUrl = image
)
