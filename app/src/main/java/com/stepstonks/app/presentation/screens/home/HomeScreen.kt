package com.stepstonks.app.presentation.screens.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stepstonks.app.domain.model.StepData
import com.stepstonks.app.domain.model.getLevel
import com.stepstonks.app.presentation.components.*
import com.stepstonks.app.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    onNavigateToWalk: () -> Unit,
    onNavigateToChallenges: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToSneakers: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(ElectricPurple.copy(0.3f), DarkBackground))
                )
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        getGreeting(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Text(
                        state.user?.username ?: "Walker",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Spacer(Modifier.height(4.dp))
                    if (state.user != null) {
                        StreakBadge(streak = state.user!!.currentStreak)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    state.user?.let { user ->
                        LevelBadge(level = user.level)
                    }
                }
            }
        }

        Column(Modifier.padding(horizontal = 16.dp)) {
            // XP Progress
            state.user?.let { user ->
                XpProgressBar(
                    xp = user.xp,
                    xpToNext = user.xpToNextLevel,
                    level = state.currentLevel,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
            }

            // Token Balance Card
            state.user?.let { user ->
                TokenBalanceCard(
                    balance = user.tokenBalance,
                    todayEarned = state.todaySteps?.tokensEarned ?: 0.0,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                EnergyBar(
                    energy = user.energy,
                    maxEnergy = user.maxEnergy,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(24.dp))

            // Step Progress Ring
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                StepProgressRing(
                    steps = state.todaySteps?.stepCount ?: 0L,
                    goalSteps = state.todaySteps?.goalSteps ?: 10_000L
                )
            }

            Spacer(Modifier.height(24.dp))

            // Quick Stats
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickStatCard(
                    emoji = "🔥",
                    label = "Calories",
                    value = "%,.0f".format(state.todaySteps?.caloriesBurned ?: 0.0),
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    emoji = "📍",
                    label = "Distance",
                    value = "%.2f km".format(state.todaySteps?.distanceKm ?: 0.0),
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    emoji = "⏱️",
                    label = "Active",
                    value = "${state.todaySteps?.activeMinutes ?: 0} min",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Weekly Chart
            WeeklyStepsChart(steps = state.weeklySteps)

            Spacer(Modifier.height(24.dp))

            // Action buttons
            Text(
                "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    emoji = "🚶",
                    label = "Start Walk",
                    color = NeonGreen,
                    onClick = onNavigateToWalk,
                    modifier = Modifier.weight(1f)
                )
                ActionButton(
                    emoji = "🏆",
                    label = "Challenges\n${if (state.completedChallengesCount > 0) "• ${state.completedChallengesCount} ready!" else "${state.activeDailyChallengesCount} active"}",
                    color = if (state.completedChallengesCount > 0) GoldYellow else ElectricPurple,
                    onClick = onNavigateToChallenges,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    emoji = "💰",
                    label = "My Wallet",
                    color = GoldYellow,
                    onClick = onNavigateToWallet,
                    modifier = Modifier.weight(1f)
                )
                state.equippedSneaker?.let { sneaker ->
                    ActionButton(
                        emoji = "👟",
                        label = "${sneaker.name}\n${sneaker.rarity.displayName}",
                        color = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(sneaker.rarity.colorHex)),
                        onClick = onNavigateToSneakers,
                        modifier = Modifier.weight(1f)
                    )
                } ?: ActionButton(
                    emoji = "👟",
                    label = "Sneakers",
                    color = NeonBlue,
                    onClick = onNavigateToSneakers,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun QuickStatCard(emoji: String, label: String, value: String, modifier: Modifier = Modifier) {
    GlowingCard(
        modifier = modifier,
        glowColor = ElectricPurple.copy(0.3f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 20.sp)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
            Text(label, fontSize = 10.sp, color = TextMuted)
        }
    }
}

@Composable
private fun ActionButton(
    emoji: String,
    label: String,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 28.sp)
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                fontWeight = FontWeight.SemiBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun WeeklyStepsChart(steps: List<StepData>) {
    if (steps.isEmpty()) return
    val maxSteps = steps.maxOfOrNull { it.stepCount } ?: 1L
    Column {
        Text("This Week", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        Spacer(Modifier.height(12.dp))
        GlowingCard(glowColor = NeonGreen.copy(0.1f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                val sdf = SimpleDateFormat("EEE", Locale.US)
                steps.takeLast(7).forEach { day ->
                    val barHeight = if (maxSteps == 0L) 0f else (day.stepCount.toFloat() / maxSteps.toFloat())
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.height(80.dp)
                    ) {
                        Box(
                            Modifier
                                .width(24.dp)
                                .weight(1f, fill = false)
                                .fillMaxHeight(barHeight.coerceAtLeast(0.04f))
                                .background(
                                    if (day.isGoalCompleted) GoldYellow else NeonGreen.copy(0.7f),
                                    RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                )
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            try { sdf.format(SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(day.date)!!) } catch (e: Exception) { "?" },
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

private fun getGreeting(): String {
    return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 5..11 -> "Good morning 🌅"
        in 12..17 -> "Good afternoon ☀️"
        in 18..21 -> "Good evening 🌆"
        else -> "Good night 🌙"
    }
}
