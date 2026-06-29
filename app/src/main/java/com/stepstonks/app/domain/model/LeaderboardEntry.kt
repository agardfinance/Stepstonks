package com.stepstonks.app.domain.model

enum class LeaderboardPeriod { DAILY, WEEKLY, ALL_TIME }
enum class LeaderboardScope { GLOBAL, FRIENDS, LOCAL }

data class LeaderboardEntry(
    val rank: Int,
    val userId: String,
    val username: String,
    val avatarUrl: String?,
    val level: Int,
    val steps: Long,
    val tokensEarned: Double,
    val sneakerRarity: SneakerRarity?,
    val isCurrentUser: Boolean = false,
    val streak: Int = 0,
    val badge: String? = null
) {
    val rankEmoji: String get() = when (rank) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> "#$rank"
    }
}

fun generateMockLeaderboard(currentUserId: String, currentUsername: String, currentSteps: Long, currentLevel: Int): List<LeaderboardEntry> {
    val mockUsers = listOf(
        Triple("CryptoWalker_Pro", 48_230L, 12),
        Triple("StepMaster_X", 41_100L, 10),
        Triple("NFTRunner", 38_750L, 9),
        Triple("BlockchainStrider", 35_200L, 11),
        Triple("TokenGainer_99", 32_800L, 8),
        Triple("WalkToEarnKing", 29_900L, 7),
        Triple("SatoshiStepper", 27_400L, 9),
        Triple("DeFiWalker", 24_100L, 6),
        Triple("AltcoinAthlete", 21_800L, 8),
        Triple("StepVault_Elite", 19_200L, 7),
    )
    val allEntries = mockUsers.mapIndexed { idx, (name, steps, level) ->
        LeaderboardEntry(
            rank = idx + 1,
            userId = "mock_$idx",
            username = name,
            avatarUrl = null,
            level = level,
            steps = steps,
            tokensEarned = steps * 0.001,
            sneakerRarity = SneakerRarity.entries.toTypedArray().getOrNull(level / 3),
            streak = (10 - idx).coerceAtLeast(1)
        )
    }.toMutableList()
    val userRank = allEntries.indexOfFirst { it.steps < currentSteps }.let { if (it == -1) allEntries.size else it } + 1
    allEntries.add(
        userRank - 1,
        LeaderboardEntry(userRank, currentUserId, currentUsername, null, currentLevel, currentSteps, currentSteps * 0.001, null, true)
    )
    return allEntries.mapIndexed { i, e -> e.copy(rank = i + 1) }.take(20)
}
