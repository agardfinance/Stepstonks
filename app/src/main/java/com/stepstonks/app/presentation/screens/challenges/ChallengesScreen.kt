package com.stepstonks.app.presentation.screens.challenges

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stepstonks.app.domain.model.Challenge
import com.stepstonks.app.domain.model.ChallengeStatus
import com.stepstonks.app.presentation.components.ChallengeCard
import com.stepstonks.app.presentation.theme.*

@Composable
fun ChallengesScreen(
    onNavigateToAchievements: () -> Unit,
    viewModel: ChallengesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Daily", "Weekly", "Special")

    val claimResult = state.claimResult
    LaunchedEffect(claimResult) {
        if (claimResult is ClaimResult.Success) {
            kotlinx.coroutines.delay(2000)
            viewModel.clearClaimResult()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(Modifier.fillMaxSize()) {
            // Header
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(GoldYellow.copy(0.15f), DarkBackground)))
                    .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Challenges", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                        Text("Complete challenges to earn bonus STK", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                    OutlinedButton(
                        onClick = onNavigateToAchievements,
                        border = BorderStroke(1.dp, GoldYellow.copy(0.5f)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = GoldYellow, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Achievements", color = GoldYellow, fontSize = 12.sp)
                    }
                }
            }

            // Tab row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkSurface,
                contentColor = NeonGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = NeonGreen
                    )
                }
            ) {
                tabs.forEachIndexed { i, tab ->
                    Tab(
                        selected = selectedTab == i,
                        onClick = { selectedTab = i },
                        text = {
                            val challenges = when (i) {
                                0 -> state.dailyChallenges
                                1 -> state.weeklyChallenges
                                else -> state.specialChallenges
                            }
                            val completedCount = challenges.count { it.status == ChallengeStatus.COMPLETED }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(tab, color = if (selectedTab == i) NeonGreen else TextMuted)
                                if (completedCount > 0) {
                                    Spacer(Modifier.width(4.dp))
                                    Box(
                                        Modifier
                                            .size(16.dp)
                                            .background(GoldYellow, RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("$completedCount", fontSize = 9.sp, color = DarkBackground, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    )
                }
            }

            // Challenges list
            val challenges = when (selectedTab) {
                0 -> state.dailyChallenges
                1 -> state.weeklyChallenges
                else -> state.specialChallenges
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (challenges.isEmpty()) {
                    Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🎯", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("No challenges yet", color = TextMuted)
                            Text("Keep walking to unlock challenges!", fontSize = 12.sp, color = TextMuted)
                        }
                    }
                } else {
                    challenges.forEach { challenge ->
                        ChallengeCard(
                            challenge = challenge,
                            onClaim = { viewModel.claimChallenge(challenge.id) }
                        )
                    }
                }
            }
        }

        // Success overlay
        AnimatedVisibility(
            visible = claimResult is ClaimResult.Success,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            if (claimResult is ClaimResult.Success) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCard),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎉", fontSize = 48.sp)
                        Text("Reward Claimed!", fontWeight = FontWeight.Bold, color = GoldYellow, fontSize = 20.sp)
                        if (claimResult.challenge.tokenReward > 0) {
                            Text("+${claimResult.challenge.tokenReward} STK", color = NeonGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        if (claimResult.challenge.xpReward > 0) {
                            Text("+${claimResult.challenge.xpReward} XP", color = ElectricPurple, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

