package com.stepstonks.app.presentation.screens.walk

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stepstonks.app.presentation.components.*
import com.stepstonks.app.presentation.theme.*

@Composable
fun WalkScreen(viewModel: WalkViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val pulseTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 1f, targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "pulseScale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    if (state.isTracking)
                        listOf(NeonGreen.copy(0.15f), DarkBackground)
                    else
                        listOf(ElectricPurple.copy(0.1f), DarkBackground)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                "Walk Mode",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )
            Text(
                if (state.isTracking) "Tracking your steps..." else "Ready to earn?",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(Modifier.height(32.dp))

            // Main step ring
            StepProgressRing(
                steps = state.todaySteps?.stepCount ?: 0L,
                goalSteps = state.todaySteps?.goalSteps ?: 10_000L,
                modifier = if (state.isTracking) Modifier.scale(pulseScale) else Modifier
            )

            Spacer(Modifier.height(32.dp))

            // Live earning stats
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EarningStat(
                    label = "Earned Today",
                    value = "%.2f STK".format(state.todaySteps?.tokensEarned ?: 0.0),
                    color = GoldYellow,
                    modifier = Modifier.weight(1f)
                )
                EarningStat(
                    label = "Rate / Step",
                    value = "%.4f STK".format(viewModel.getCurrentEarningRate()),
                    color = NeonGreen,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Sneaker info
            state.equippedSneaker?.let { sneaker ->
                GlowingCard(
                    glowColor = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(sneaker.rarity.colorHex)).copy(0.3f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("👟", fontSize = 32.sp)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(sneaker.name, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                            Text(
                                "${sneaker.rarity.displayName} • ${sneaker.rarity.earningMultiplier}x multiplier",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("⚡ ${sneaker.durability.toInt()}%", fontSize = 12.sp, color = if (sneaker.durability > 30) NeonGreen else CrimsonRed)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Energy bar
            state.user?.let { user ->
                EnergyBar(energy = user.energy, maxEnergy = user.maxEnergy)
            }

            Spacer(Modifier.weight(1f))

            // Start/Stop button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .scale(if (state.isTracking) pulseScale else 1f)
            ) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                if (state.isTracking)
                                    listOf(CrimsonRed.copy(0.3f), CrimsonRed.copy(0.1f))
                                else
                                    listOf(NeonGreen.copy(0.3f), NeonGreen.copy(0.1f))
                            ),
                            CircleShape
                        )
                        .border(
                            3.dp,
                            if (state.isTracking) CrimsonRed else NeonGreen,
                            CircleShape
                        )
                )
                IconButton(
                    onClick = {
                        if (state.isTracking) viewModel.stopWalking()
                        else viewModel.startWalking()
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        if (state.isTracking) Icons.Filled.Stop else Icons.Filled.PlayArrow,
                        contentDescription = null,
                        tint = if (state.isTracking) CrimsonRed else NeonGreen,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            Text(
                if (state.isTracking) "TAP TO STOP" else "TAP TO START",
                fontSize = 12.sp,
                color = if (state.isTracking) CrimsonRed else NeonGreen,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun EarningStat(label: String, value: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    GlowingCard(modifier = modifier, glowColor = color.copy(0.2f)) {
        Column(
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.ExtraBold, color = color, fontSize = 16.sp)
            Text(label, fontSize = 11.sp, color = TextMuted)
        }
    }
}
