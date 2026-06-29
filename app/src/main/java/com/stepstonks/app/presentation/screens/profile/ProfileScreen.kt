package com.stepstonks.app.presentation.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stepstonks.app.presentation.components.*
import com.stepstonks.app.presentation.theme.*

@Composable
fun ProfileScreen(
    onNavigateToSneakers: () -> Unit,
    onNavigateToAchievements: () -> Unit,
    onNavigateToWallet: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Profile header
        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(ElectricPurple.copy(0.35f), NeonGreen.copy(0.1f), DarkBackground)
                    )
                )
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Avatar
                Box(
                    Modifier
                        .size(90.dp)
                        .background(
                            Brush.radialGradient(listOf(ElectricPurple, NeonGreen.copy(0.5f))),
                            CircleShape
                        )
                        .border(3.dp, GoldYellow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        state.user?.username?.take(1)?.uppercase() ?: "?",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    state.user?.username ?: "Walker",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LevelBadge(level = state.user?.level ?: 1)
                    Text(state.currentLevel.title, style = MaterialTheme.typography.bodyMedium, color = GoldYellow)
                }
                Spacer(Modifier.height(8.dp))
                state.user?.let { user ->
                    StreakBadge(streak = user.currentStreak)
                    Spacer(Modifier.height(8.dp))
                    XpProgressBar(
                        xp = user.xp,
                        xpToNext = user.xpToNextLevel,
                        level = state.currentLevel,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Column(Modifier.padding(16.dp)) {
            // Level perks
            if (state.currentLevel.perks.isNotEmpty()) {
                GlowingCard(glowColor = ElectricPurple.copy(0.2f), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Level ${state.currentLevel.number} Perks", fontWeight = FontWeight.Bold, color = GoldYellow)
                        Spacer(Modifier.height(8.dp))
                        state.currentLevel.perks.forEach { perk ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("✓", color = NeonGreen, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.width(8.dp))
                                Text(perk, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Stats grid
            Text("My Statistics", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("👟", "Total Steps", "%,d".format(state.totalSteps), NeonGreen, Modifier.weight(1f))
                StatCard("💰", "Total Earned", "%.0f STK".format(state.totalTokensEarned), GoldYellow, Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard("🔥", "Longest Streak", "${state.user?.longestStreak ?: 0}d", NeonOrange, Modifier.weight(1f))
                StatCard("🏆", "Achievements", "${state.claimedAchievements}", ElectricPurple, Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))

            // Navigation cards
            Text("My Collection", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Spacer(Modifier.height(12.dp))
            ProfileNavCard(
                icon = Icons.Filled.SportsHandball,
                title = "My Sneakers",
                subtitle = "${state.sneakerCount} sneakers in collection",
                onClick = onNavigateToSneakers
            )
            Spacer(Modifier.height(8.dp))
            ProfileNavCard(
                icon = Icons.Filled.EmojiEvents,
                title = "Achievements",
                subtitle = "${state.claimedAchievements} badges earned",
                onClick = onNavigateToAchievements
            )
            Spacer(Modifier.height(8.dp))
            ProfileNavCard(
                icon = Icons.Filled.AccountBalanceWallet,
                title = "Wallet",
                subtitle = "${"%,.2f".format(state.user?.tokenBalance ?: 0.0)} STK available",
                onClick = onNavigateToWallet
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatCard(emoji: String, label: String, value: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color.copy(0.08f), RoundedCornerShape(12.dp))
            .border(1.dp, color.copy(0.25f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(emoji, fontSize = 22.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.ExtraBold, color = color, fontSize = 18.sp)
            Text(label, fontSize = 11.sp, color = TextMuted)
        }
    }
}

@Composable
private fun ProfileNavCard(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, DarkBorder)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Text(subtitle, fontSize = 12.sp, color = TextMuted)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = TextMuted)
        }
    }
}
