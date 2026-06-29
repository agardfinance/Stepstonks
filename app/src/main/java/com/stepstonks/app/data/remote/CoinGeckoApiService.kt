package com.stepstonks.app.data.remote

import com.stepstonks.app.data.remote.dto.CoinDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinGeckoApiService {

    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") currency: String = "usd",
        @Query("ids") ids: String = COIN_IDS,
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 20,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = true,
        @Query("price_change_percentage") priceChangePercentage: String = "24h"
    ): List<CoinDto>

    companion object {
        const val BASE_URL = "https://api.coingecko.com/api/v3/"
        const val COIN_IDS = "bitcoin,ethereum,binancecoin,solana,ripple,cardano,avalanche-2,dogecoin,polkadot,matic-network,chainlink,cosmos,uniswap,litecoin,toncoin,tron"
    }
}
