package com.stepstonks.app.domain.model

data class CryptoAsset(
    val id: String,
    val symbol: String,
    val name: String,
    val currentPrice: Double,
    val priceChange24h: Double,
    val marketCap: Double,
    val marketCapRank: Int,
    val sparkline: List<Double> = emptyList(),
    val imageUrl: String = "",
    val isSTK: Boolean = false
) {
    val priceFormatted: String get() = when {
        currentPrice >= 1_000 -> "$%,.0f".format(currentPrice)
        currentPrice >= 1 -> "$%,.2f".format(currentPrice)
        currentPrice >= 0.01 -> "$%.4f".format(currentPrice)
        else -> "$%.6f".format(currentPrice)
    }

    val changeFormatted: String get() = "%+.2f%%".format(priceChange24h)
    val isPositive: Boolean get() = priceChange24h >= 0
}

// Emoji icons for each coin symbol
fun cryptoEmoji(symbol: String): String = when (symbol.uppercase()) {
    "BTC" -> "₿"
    "ETH" -> "Ξ"
    "BNB" -> "B"
    "SOL" -> "◎"
    "XRP" -> "✕"
    "ADA" -> "₳"
    "AVAX" -> "A"
    "DOGE" -> "Ð"
    "DOT" -> "●"
    "MATIC", "POL" -> "M"
    "LINK" -> "⬡"
    "ATOM" -> "⚛"
    "UNI" -> "🦄"
    "LTC" -> "Ł"
    "TON" -> "💎"
    "TRX" -> "T"
    "STK" -> "👟"
    else -> symbol.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
}

val FALLBACK_CRYPTO = listOf(
    CryptoAsset("stepstonks", "STK", "Stepstonks", 0.01, 2.5, 10_000_000.0, 999, isSTK = true),
    CryptoAsset("bitcoin", "BTC", "Bitcoin", 67_432.0, 1.82, 1_329_000_000_000.0, 1),
    CryptoAsset("ethereum", "ETH", "Ethereum", 3_521.0, 2.14, 423_000_000_000.0, 2),
    CryptoAsset("binancecoin", "BNB", "BNB", 604.0, -0.73, 88_000_000_000.0, 3),
    CryptoAsset("solana", "SOL", "Solana", 178.0, 3.41, 82_000_000_000.0, 4),
    CryptoAsset("ripple", "XRP", "XRP", 0.5273, -1.12, 29_000_000_000.0, 5),
    CryptoAsset("cardano", "ADA", "Cardano", 0.4621, 0.95, 16_000_000_000.0, 6),
    CryptoAsset("avalanche-2", "AVAX", "Avalanche", 38.42, -2.31, 15_700_000_000.0, 7),
    CryptoAsset("dogecoin", "DOGE", "Dogecoin", 0.1634, 4.22, 23_500_000_000.0, 8),
    CryptoAsset("polkadot", "DOT", "Polkadot", 8.51, -0.44, 12_000_000_000.0, 9),
    CryptoAsset("matic-network", "MATIC", "Polygon", 0.8834, 1.65, 8_200_000_000.0, 10),
    CryptoAsset("chainlink", "LINK", "Chainlink", 18.23, 3.78, 10_700_000_000.0, 11),
    CryptoAsset("cosmos", "ATOM", "Cosmos", 9.44, -1.02, 3_600_000_000.0, 12),
    CryptoAsset("uniswap", "UNI", "Uniswap", 9.12, 2.05, 5_500_000_000.0, 13),
    CryptoAsset("litecoin", "LTC", "Litecoin", 85.4, -0.55, 6_300_000_000.0, 14),
    CryptoAsset("toncoin", "TON", "Toncoin", 5.83, 1.24, 14_300_000_000.0, 15),
    CryptoAsset("tron", "TRX", "TRON", 0.1245, 0.81, 10_800_000_000.0, 16),
)
