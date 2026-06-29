package com.stepstonks.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.stepstonks.app.domain.model.LeaderboardEntry
import com.stepstonks.app.domain.model.SneakerRarity
import com.stepstonks.app.presentation.theme.*

@Composable
fun LeaderboardItem(entry: LeaderboardEntry, modifier: Modifier = Modifier) {
    val bgColor = when {
        entry.isCurrentUser -> ElectricPurple.copy(alpha = 0.15f)
        entry.rank == 1 -> GoldYellow.copy(alpha = 0.1f)
        entry.rank == 2 -> TextSecondary.copy(alpha = 0.08f)
        entry.rank == 3 -> NeonOrange.copy(alpha = 0.08f)
        else -> DarkCard
    }
    val borderColor = when {
        entry.isCurrentUser -> ElectricPurple.copy(0.5f)
        entry.rank <= 3 -> when (entry.rank) {
            1 -> GoldYellow.copy(0.5f)
            2 -> TextSecondary.copy(0.4f)
            else -> NeonOrange.copy(0.4f)
        }
        else -> DarkBorder
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(36.dp),
            contentAlignment = Alignment.Center
        ) {
            if (entry.rank <= 3) {
                Text(entry.rankEmoji, fontSize = 22.sp)
            } else {
                Text(
                    "#${entry.rank}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Box(
            Modifier
                .size(40.dp)
                .background(
                    Brush.radialGradient(listOf(ElectricPurple.copy(0.4f), DarkBorder)),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                entry.username.take(1).uppercase(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    entry.username,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (entry.isCurrentUser) NeonGreen else TextPrimary,
                    fontWeight = if (entry.isCurrentUser) FontWeight.Bold else FontWeight.SemiBold
                )
                if (entry.isCurrentUser) {
                    Spacer(Modifier.width(4.dp))
                    Text("(You)", fontSize = 11.sp, color = NeonGreen)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Lv.${entry.level}", fontSize = 11.sp, color = GoldYellow)
                Spacer(Modifier.width(6.dp))
                if (entry.streak > 0) Text("🔥${entry.streak}", fontSize = 11.sp, color = NeonOrange)
                entry.sneakerRarity?.let { rarity ->
                    Spacer(Modifier.width(6.dp))
                    Text("👟 ${rarity.displayName}", fontSize = 11.sp, color = TextMuted)
                }
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                "%,d".format(entry.steps),
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontSize = 14.sp
            )
            Text("steps", fontSize = 10.sp, color = TextMuted)
            Text(
                "+${String.format("%.0f", entry.tokensEarned)} STK",
                fontSize = 11.sp,
                color = GoldYellow,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
