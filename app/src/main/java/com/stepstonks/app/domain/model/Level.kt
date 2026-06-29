package com.stepstonks.app.domain.model

data class Level(
    val number: Int,
    val requiredXp: Long,
    val title: String,
    val perks: List<String>,
    val maxEnergy: Float,
    val sneakerSlots: Int
)

private data class LevelSeed(val title: String, val perks: List<String>, val energy: Float, val slots: Int)

val LEVEL_TABLE = buildList {
    val levelData = listOf(
        LevelSeed("Beginner", listOf("Start earning STK"), 20f, 1),
        LevelSeed("Walker", listOf("Unlock Daily Challenges", "+5% earning"), 22f, 1),
        LevelSeed("Strider", listOf("Unlock Weekly Challenges", "+5% energy"), 24f, 1),
        LevelSeed("Jogger", listOf("Unlock Sneaker Shop", "+10% earning"), 24f, 2),
        LevelSeed("Runner", listOf("Unlock Referrals", "+10% energy"), 26f, 2),
        LevelSeed("Sprinter", listOf("Unlock Squad features", "+15% earning"), 26f, 2),
        LevelSeed("Athlete", listOf("Unlock NFT Minting", "+15% energy"), 28f, 2),
        LevelSeed("Champion", listOf("Unlock Mystery Boxes", "+20% earning"), 28f, 3),
        LevelSeed("Legend", listOf("Unlock Special Events", "+20% energy"), 30f, 3),
        LevelSeed("Master", listOf("Unlock all features", "+25% earning bonus"), 30f, 3),
        LevelSeed("Grand Master", listOf("Max earning rate", "Exclusive NFT drops"), 32f, 4),
        LevelSeed("Titan", listOf("Priority withdrawals", "Governance voting"), 32f, 4),
        LevelSeed("Demigod", listOf("Beta feature access", "Monthly airdrop"), 34f, 4),
        LevelSeed("Deity", listOf("Creator tools access", "Revenue sharing"), 34f, 5),
        LevelSeed("Immortal", listOf("All features unlocked", "Legendary status"), 36f, 5),
    )
    var xpRequired = 500L
    levelData.forEachIndexed { index, (title, perks, energy, slots) ->
        add(Level(index + 1, xpRequired, title, perks, energy, slots))
        xpRequired = (xpRequired * 1.5).toLong()
    }
    repeat(35) { extra ->
        val level = extra + 16
        val title = when {
            level < 20 -> "Elite ${level}"
            level < 30 -> "Apex ${level}"
            level < 40 -> "Cosmic ${level}"
            else -> "Transcendent ${level}"
        }
        add(Level(level, xpRequired, title, listOf("Elite status", "Max bonuses"), 36f + extra * 0.2f, 5))
        xpRequired = (xpRequired * 1.5).toLong()
    }
}

fun getLevel(number: Int): Level =
    LEVEL_TABLE.find { it.number == number } ?: LEVEL_TABLE.last()

fun getLevelForXp(totalXp: Long): Pair<Level, Long> {
    var accumulated = 0L
    for (level in LEVEL_TABLE) {
        if (accumulated + level.requiredXp > totalXp) {
            return Pair(level, totalXp - accumulated)
        }
        accumulated += level.requiredXp
        if (level == LEVEL_TABLE.last()) return Pair(level, level.requiredXp)
    }
    return Pair(LEVEL_TABLE.last(), LEVEL_TABLE.last().requiredXp)
}
