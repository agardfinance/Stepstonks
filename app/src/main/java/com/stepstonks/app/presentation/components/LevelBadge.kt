package com.stepstonks.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.stepstonks.app.domain.model.Level
import com.stepstonks.app.presentation.theme.*

@Composable
fun LevelBadge(level: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(44.dp)
            .background(
                Brush.radialGradient(listOf(ElectricPurple, NeonGreen.copy(0.5f))),
                CircleShape
            )
            .border(2.dp, GoldYellow, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$level",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary
        )
    }
}

@Composable
fun XpProgressBar(
    xp: Long,
    xpToNext: Long,
    level: Level,
    modifier: Modifier = Modifier
) {
    val progress = if (xpToNext == 0L) 1f else (xp.toFloat() / xpToNext.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(progress, animationSpec = tween(800), label = "xp")

    Column(modifier = modifier) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Lv.${level.number} • ${level.title}",
                style = MaterialTheme.typography.bodySmall,
                color = GoldYellow,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "${xp} / ${xpToNext} XP",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }
        Spacer(Modifier.height(6.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(DarkBorder, RoundedCornerShape(5.dp))
        ) {
            Box(
                Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(listOf(ElectricPurple, GoldYellow)),
                        RoundedCornerShape(5.dp)
                    )
            )
        }
    }
}

@Composable
fun StreakBadge(streak: Int, modifier: Modifier = Modifier) {
    if (streak == 0) return
    Row(
        modifier = modifier
            .background(
                Brush.horizontalGradient(listOf(NeonOrange.copy(0.2f), GoldYellow.copy(0.2f))),
                RoundedCornerShape(20.dp)
            )
            .border(1.dp, NeonOrange.copy(0.5f), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("🔥", fontSize = 14.sp)
        Spacer(Modifier.width(4.dp))
        Text(
            "$streak day streak",
            style = MaterialTheme.typography.bodySmall,
            color = GoldYellow,
            fontWeight = FontWeight.Bold
        )
    }
}
