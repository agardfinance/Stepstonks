package com.stepstonks.app.domain.model

enum class ChallengeType { DAILY, WEEKLY, MONTHLY, SPECIAL, ACHIEVEMENT }
enum class ChallengeCategory { STEPS, DISTANCE, STREAK, SOCIAL, TOKENS, LEVEL }
enum class ChallengeStatus { LOCKED, ACTIVE, COMPLETED, CLAIMED }

data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val type: ChallengeType,
    val category: ChallengeCategory,
    val status: ChallengeStatus = ChallengeStatus.ACTIVE,
    val targetValue: Long,
    val currentValue: Long = 0L,
    val tokenReward: Double = 0.0,
    val xpReward: Long = 0L,
    val gemReward: Int = 0,
    val iconEmoji: String = "🏃",
    val startsAt: Long = System.currentTimeMillis(),
    val endsAt: Long? = null,
    val completedAt: Long? = null,
    val requiredLevel: Int = 1
) {
    val progressPercent: Float
        get() = if (targetValue == 0L) 1f else (currentValue.toFloat() / targetValue.toFloat()).coerceIn(0f, 1f)

    val isCompleted: Boolean
        get() = currentValue >= targetValue

    val timeRemainingMs: Long?
        get() = endsAt?.let { it - System.currentTimeMillis() }
}

val PREDEFINED_DAILY_CHALLENGES = listOf(
    Challenge("daily_steps_5k", "5K Steps", "Walk 5,000 steps today", ChallengeType.DAILY, ChallengeCategory.STEPS, targetValue = 5_000, tokenReward = 25.0, xpReward = 50, iconEmoji = "👟"),
    Challenge("daily_steps_10k", "10K Steps", "Walk 10,000 steps today", ChallengeType.DAILY, ChallengeCategory.STEPS, targetValue = 10_000, tokenReward = 60.0, xpReward = 120, iconEmoji = "🏃"),
    Challenge("daily_steps_15k", "Power Walker", "Walk 15,000 steps today", ChallengeType.DAILY, ChallengeCategory.STEPS, targetValue = 15_000, tokenReward = 100.0, xpReward = 200, iconEmoji = "⚡"),
    Challenge("daily_distance_5km", "5KM Journey", "Cover 5 km today", ChallengeType.DAILY, ChallengeCategory.DISTANCE, targetValue = 5, tokenReward = 50.0, xpReward = 100, iconEmoji = "🗺️"),
    Challenge("daily_earn_tokens", "Token Hunter", "Earn 50 STK today", ChallengeType.DAILY, ChallengeCategory.TOKENS, targetValue = 50, tokenReward = 20.0, xpReward = 40, iconEmoji = "💰"),
)

val PREDEFINED_WEEKLY_CHALLENGES = listOf(
    Challenge("weekly_steps_50k", "50K Warrior", "Walk 50,000 steps this week", ChallengeType.WEEKLY, ChallengeCategory.STEPS, targetValue = 50_000, tokenReward = 300.0, xpReward = 600, iconEmoji = "🛡️"),
    Challenge("weekly_steps_70k", "Marathon Week", "Walk 70,000 steps this week", ChallengeType.WEEKLY, ChallengeCategory.STEPS, targetValue = 70_000, tokenReward = 500.0, xpReward = 1000, iconEmoji = "🏅"),
    Challenge("weekly_streak_7", "7 Day Streak", "Walk every day for 7 days", ChallengeType.WEEKLY, ChallengeCategory.STREAK, targetValue = 7, tokenReward = 400.0, xpReward = 800, iconEmoji = "🔥"),
    Challenge("weekly_distance_30km", "30KM Trek", "Cover 30 km this week", ChallengeType.WEEKLY, ChallengeCategory.DISTANCE, targetValue = 30, tokenReward = 350.0, xpReward = 700, iconEmoji = "🌍"),
)

val PREDEFINED_ACHIEVEMENTS = listOf(
    Challenge("ach_first_steps", "First Steps", "Take your first 100 steps", ChallengeType.ACHIEVEMENT, ChallengeCategory.STEPS, targetValue = 100, tokenReward = 10.0, xpReward = 50, iconEmoji = "👶"),
    Challenge("ach_step_1k", "Step Starter", "Walk 1,000 steps total", ChallengeType.ACHIEVEMENT, ChallengeCategory.STEPS, targetValue = 1_000, tokenReward = 20.0, xpReward = 100, iconEmoji = "🌱"),
    Challenge("ach_step_10k", "Step Explorer", "Walk 10,000 steps total", ChallengeType.ACHIEVEMENT, ChallengeCategory.STEPS, targetValue = 10_000, tokenReward = 50.0, xpReward = 250, iconEmoji = "🗺️"),
    Challenge("ach_step_100k", "Step Champion", "Walk 100,000 steps total", ChallengeType.ACHIEVEMENT, ChallengeCategory.STEPS, targetValue = 100_000, tokenReward = 200.0, xpReward = 1000, iconEmoji = "🏆"),
    Challenge("ach_step_1m", "Step Legend", "Walk 1,000,000 steps total", ChallengeType.ACHIEVEMENT, ChallengeCategory.STEPS, targetValue = 1_000_000, tokenReward = 2000.0, xpReward = 10000, iconEmoji = "👑"),
    Challenge("ach_streak_3", "3 Day Streak", "Walk 3 days in a row", ChallengeType.ACHIEVEMENT, ChallengeCategory.STREAK, targetValue = 3, tokenReward = 30.0, xpReward = 150, iconEmoji = "🔥"),
    Challenge("ach_streak_7", "Week Warrior", "Walk 7 days in a row", ChallengeType.ACHIEVEMENT, ChallengeCategory.STREAK, targetValue = 7, tokenReward = 100.0, xpReward = 500, iconEmoji = "⚔️"),
    Challenge("ach_streak_30", "Month Master", "Walk 30 days in a row", ChallengeType.ACHIEVEMENT, ChallengeCategory.STREAK, targetValue = 30, tokenReward = 500.0, xpReward = 2500, iconEmoji = "🌙"),
    Challenge("ach_level_5", "Level 5", "Reach level 5", ChallengeType.ACHIEVEMENT, ChallengeCategory.LEVEL, targetValue = 5, tokenReward = 100.0, xpReward = 0, gemReward = 50, iconEmoji = "⭐"),
    Challenge("ach_level_10", "Level 10", "Reach level 10", ChallengeType.ACHIEVEMENT, ChallengeCategory.LEVEL, targetValue = 10, tokenReward = 300.0, xpReward = 0, gemReward = 150, iconEmoji = "🌟"),
    Challenge("ach_level_20", "Level 20", "Reach level 20", ChallengeType.ACHIEVEMENT, ChallengeCategory.LEVEL, targetValue = 20, tokenReward = 1000.0, xpReward = 0, gemReward = 500, iconEmoji = "💫"),
    Challenge("ach_tokens_1000", "Crypto Newbie", "Earn 1,000 STK total", ChallengeType.ACHIEVEMENT, ChallengeCategory.TOKENS, targetValue = 1000, tokenReward = 100.0, xpReward = 500, iconEmoji = "💎"),
    Challenge("ach_tokens_10000", "Crypto King", "Earn 10,000 STK total", ChallengeType.ACHIEVEMENT, ChallengeCategory.TOKENS, targetValue = 10000, tokenReward = 1000.0, xpReward = 5000, iconEmoji = "👑"),
)
