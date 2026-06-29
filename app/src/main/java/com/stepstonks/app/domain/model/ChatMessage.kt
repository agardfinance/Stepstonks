package com.stepstonks.app.domain.model

data class ChatMessage(
    val id: String,
    val userId: String,
    val username: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val messageType: MessageType = MessageType.TEXT,
    val reactions: Map<String, Int> = emptyMap(),
    val isCurrentUser: Boolean = false
)

enum class MessageType { TEXT, ACHIEVEMENT, CHALLENGE_COMPLETE, JOINED }

val MOCK_MESSAGES = listOf(
    ChatMessage("1", "u1", "CryptoWalker", "Just hit 10,000 steps today! 🎉", System.currentTimeMillis() - 3_600_000, reactions = mapOf("🔥" to 5, "👏" to 3)),
    ChatMessage("2", "u2", "NeonStrider", "Anyone else using Epic sneakers? My earning rate is insane rn 🚀", System.currentTimeMillis() - 3_000_000, reactions = mapOf("💎" to 8)),
    ChatMessage("3", "u3", "StepstonksPro", "🏆 ACHIEVEMENT UNLOCKED: Marathon Walker — 100,000 total steps!", System.currentTimeMillis() - 2_400_000, messageType = MessageType.ACHIEVEMENT, reactions = mapOf("🎉" to 12, "🔥" to 7)),
    ChatMessage("4", "u4", "WalkToEarn99", "What's the best time to walk for max STK? Morning or evening?", System.currentTimeMillis() - 1_800_000),
    ChatMessage("5", "u2", "NeonStrider", "Morning walks give better streak bonuses imo 💡", System.currentTimeMillis() - 1_500_000, reactions = mapOf("💡" to 4)),
    ChatMessage("6", "u5", "TokenHunter", "Just bought my first Rare sneaker from the shop! Let's gooo 🚀", System.currentTimeMillis() - 900_000, reactions = mapOf("🚀" to 6, "👟" to 3)),
    ChatMessage("7", "u6", "CryptoFitness", "✅ CHALLENGE COMPLETED: 5K Sprint — earned 50 STK!", System.currentTimeMillis() - 600_000, messageType = MessageType.CHALLENGE_COMPLETE, reactions = mapOf("💪" to 9)),
    ChatMessage("8", "u7", "BlockchainRunner", "Has anyone reached level 20 yet? What perks do you get?", System.currentTimeMillis() - 300_000),
    ChatMessage("9", "u1", "CryptoWalker", "Level 20 unlocks Legendary sneaker crafting! It's wild 🔥", System.currentTimeMillis() - 180_000, reactions = mapOf("😮" to 5)),
    ChatMessage("10", "u8", "MoonWalker_X", "This app changed my life fr. Lost 5kg and gained STK! 💪", System.currentTimeMillis() - 60_000, reactions = mapOf("❤️" to 15, "💪" to 8)),
)

val NEW_MESSAGES = listOf(
    "Walking 15k steps today, who's with me? 🏃",
    "Just upgraded my sneaker to level 3! Efficiency +20%",
    "STK price looking bullish today 📈",
    "Morning walk done ✅ 8,432 steps before 8am!",
    "Anyone in the NYC walking squad? Let's sync challenges 🗽",
    "Pro tip: always equip your highest efficiency sneaker before walking 👟",
    "Hit my weekly goal 3 days early! Steak at 14 days 🔥",
)
