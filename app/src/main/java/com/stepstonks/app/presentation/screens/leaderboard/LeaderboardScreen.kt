package com.stepstonks.app.presentation.screens.leaderboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stepstonks.app.domain.model.LeaderboardPeriod
import com.stepstonks.app.presentation.components.LeaderboardItem
import com.stepstonks.app.presentation.theme.*

@Composable
fun LeaderboardScreen(viewModel: LeaderboardViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val periods = listOf(LeaderboardPeriod.DAILY, LeaderboardPeriod.WEEKLY, LeaderboardPeriod.ALL_TIME)
    val periodLabels = listOf("Today", "This Week", "All Time")

    Column(
        Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(GoldYellow.copy(0.2f), DarkBackground)))
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
        ) {
            Column {
                Text("🏆 Leaderboard", style = MaterialTheme.typography.headlineMedium, color = TextPrimary)
                Text("Compete with walkers worldwide", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }

        // Period selector
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            periods.forEachIndexed { i, period ->
                val selected = state.selectedPeriod == period
                FilterChip(
                    selected = selected,
                    onClick = { viewModel.selectPeriod(period) },
                    label = { Text(periodLabels[i], fontSize = 13.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldYellow,
                        selectedLabelColor = DarkBackground,
                        containerColor = DarkCard,
                        labelColor = TextSecondary
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Top 3 podium
        if (state.entries.size >= 3) {
            PodiumSection(entries = state.entries.take(3))
        }

        // Full list
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Full Rankings",
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            state.entries.forEach { entry ->
                LeaderboardItem(entry = entry)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PodiumSection(entries: List<com.stepstonks.app.domain.model.LeaderboardEntry>) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        // 2nd place
        PodiumEntry(entry = entries[1], podiumHeight = 80.dp, color = TextSecondary)
        // 1st place
        PodiumEntry(entry = entries[0], podiumHeight = 110.dp, color = GoldYellow, isFirst = true)
        // 3rd place
        PodiumEntry(entry = entries[2], podiumHeight = 60.dp, color = NeonOrange)
    }
}

@Composable
private fun PodiumEntry(
    entry: com.stepstonks.app.domain.model.LeaderboardEntry,
    podiumHeight: androidx.compose.ui.unit.Dp,
    color: androidx.compose.ui.graphics.Color,
    isFirst: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(entry.rankEmoji, fontSize = if (isFirst) 32.sp else 24.sp)
        Text(
            entry.username.take(8),
            fontSize = if (isFirst) 13.sp else 11.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Text("%,d".format(entry.steps), fontSize = 11.sp, color = color)
        Spacer(Modifier.height(4.dp))
        Box(
            Modifier
                .width(70.dp)
                .height(podiumHeight)
                .background(
                    Brush.verticalGradient(listOf(color.copy(0.5f), color.copy(0.2f))),
                    RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("${entry.rank}", fontWeight = FontWeight.ExtraBold, color = color, fontSize = 20.sp)
        }
    }
}
