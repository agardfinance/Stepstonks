package com.stepstonks.app.presentation.screens.achievements

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stepstonks.app.domain.model.ChallengeStatus
import com.stepstonks.app.presentation.components.ChallengeCard
import com.stepstonks.app.presentation.theme.*

@Composable
fun AchievementsScreen(viewModel: AchievementsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Header
        Box(
            Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(GoldYellow.copy(0.2f), DarkBackground)))
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
        ) {
            Column {
                Text("Achievements", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                Text("${state.claimedCount} / ${state.totalCount} unlocked", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(Modifier.height(12.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(DarkBorder, RoundedCornerShape(4.dp))
                ) {
                    val progress = if (state.totalCount == 0) 0f else state.claimedCount.toFloat() / state.totalCount.toFloat()
                    Box(
                        Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(listOf(GoldYellow.copy(0.6f), GoldYellow)),
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.achievements.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏅", fontSize = 48.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("Start walking to unlock achievements!", color = TextMuted, textAlign = TextAlign.Center)
                    }
                }
            } else {
                // Group by status
                val claimable = state.achievements.filter { it.status == ChallengeStatus.COMPLETED }
                val active = state.achievements.filter { it.status == ChallengeStatus.ACTIVE }
                val claimed = state.achievements.filter { it.status == ChallengeStatus.CLAIMED }

                if (claimable.isNotEmpty()) {
                    Text("🎉 Ready to Claim!", fontWeight = FontWeight.Bold, color = GoldYellow)
                    claimable.forEach { achievement ->
                        ChallengeCard(
                            challenge = achievement,
                            onClaim = { viewModel.claimAchievement(achievement.id) }
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
                if (active.isNotEmpty()) {
                    Text("🎯 In Progress", fontWeight = FontWeight.Bold, color = TextPrimary)
                    active.forEach { achievement ->
                        ChallengeCard(
                            challenge = achievement,
                            onClaim = {}
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                }
                if (claimed.isNotEmpty()) {
                    Text("✅ Completed", fontWeight = FontWeight.Bold, color = TextMuted)
                    claimed.forEach { achievement ->
                        ChallengeCard(
                            challenge = achievement,
                            onClaim = {}
                        )
                    }
                }
            }
        }
    }
}
