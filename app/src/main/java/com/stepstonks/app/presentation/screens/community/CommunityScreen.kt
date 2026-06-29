package com.stepstonks.app.presentation.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stepstonks.app.presentation.screens.community.chat.ChatScreen
import com.stepstonks.app.presentation.screens.community.forum.ForumScreen
import com.stepstonks.app.presentation.screens.leaderboard.LeaderboardScreen
import com.stepstonks.app.presentation.theme.*

private data class CommunityTab(val label: String, val icon: ImageVector, val emoji: String)

private val TABS = listOf(
    CommunityTab("Chat", Icons.Filled.Forum, "💬"),
    CommunityTab("Forum", Icons.Filled.Article, "📋"),
    CommunityTab("Leaderboard", Icons.Filled.Leaderboard, "🏆"),
)

@Composable
fun CommunityScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize().background(DarkBackground)) {
        // Header gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(NeonBlue.copy(0.2f), DarkBackground)))
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 4.dp)
        ) {
            Text(
                "Community",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // Tab row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurface,
            contentColor = NeonBlue,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(2.dp)
                        .background(Brush.horizontalGradient(listOf(ElectricPurple, NeonBlue)))
                )
            }
        ) {
            TABS.forEachIndexed { i, tab ->
                Tab(
                    selected = selectedTab == i,
                    onClick = { selectedTab = i },
                    text = {
                        Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)) {
                            Text(tab.emoji, fontSize = 14.sp)
                            Text(
                                tab.label,
                                color = if (selectedTab == i) NeonBlue else TextMuted,
                                fontWeight = if (selectedTab == i) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    }
                )
            }
        }

        // Content
        when (selectedTab) {
            0 -> ChatScreen()
            1 -> ForumScreen()
            2 -> LeaderboardScreen()
        }
    }
}
