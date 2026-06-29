package com.stepstonks.app

import com.stepstonks.app.domain.model.*
import org.junit.Assert.*
import org.junit.Test

class StepstonksUnitTest {

    @Test
    fun `user earning multiplier scales with level and streak`() {
        val user = User(id = "1", username = "Test", email = "", level = 5, currentStreak = 10)
        val multiplier = user.earningMultiplier()
        assertTrue("Multiplier should be > 1.0", multiplier > 1.0)
    }

    @Test
    fun `step data goal progress is clamped between 0 and 1`() {
        val stepData = StepData(date = "2025-01-01", stepCount = 15_000, goalSteps = 10_000)
        assertEquals(1f, stepData.goalProgressPercent)
    }

    @Test
    fun `challenge progress percent is calculated correctly`() {
        val challenge = Challenge(
            id = "test", title = "Test", description = "", type = ChallengeType.DAILY,
            category = ChallengeCategory.STEPS, targetValue = 1000, currentValue = 500
        )
        assertEquals(0.5f, challenge.progressPercent)
    }

    @Test
    fun `level table contains valid entries`() {
        assertTrue(LEVEL_TABLE.isNotEmpty())
        assertEquals(1, LEVEL_TABLE.first().number)
        LEVEL_TABLE.forEach { level ->
            assertTrue("Level ${level.number} should have positive XP", level.requiredXp > 0)
        }
    }

    @Test
    fun `token transaction display amount is correct`() {
        val earning = TokenTransaction(type = TransactionType.EARNED_WALKING, amount = 10.5, description = "Walking")
        val spending = TokenTransaction(type = TransactionType.SPENT_SNEAKER_PURCHASE, amount = 100.0, description = "Buy sneaker")
        assertTrue(earning.displayAmount.startsWith("+"))
        assertTrue(spending.displayAmount.startsWith("-"))
    }

    @Test
    fun `sneaker earning rate scales with rarity and attributes`() {
        val common = STARTER_SNEAKER
        val rare = Sneaker("rare_test", "Rare Test", SneakerRarity.RARE, SneakerType.JOGGER,
            attributes = SneakerAttribute(efficiency = 10))
        assertTrue("Rare should earn more than common", rare.earningRate > common.earningRate)
    }
}
