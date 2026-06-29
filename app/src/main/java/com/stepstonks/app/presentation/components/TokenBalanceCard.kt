package com.stepstonks.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.stepstonks.app.presentation.theme.*

@Composable
fun TokenBalanceCard(
    balance: Double,
    todayEarned: Double,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Brush.linearGradient(
                    listOf(
                        ElectricPurple.copy(alpha = 0.4f),
                        NeonGreen.copy(alpha = 0.15f)
                    )
                ),
                RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💎", fontSize = 20.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    "STEPSTONKS Balance",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "%,.2f STK".format(balance),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GoldYellow
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.TrendingUp, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "+%,.2f STK today".format(todayEarned),
                    style = MaterialTheme.typography.bodySmall,
                    color = NeonGreen
                )
            }
        }
    }
}

@Composable
fun EnergyBar(
    energy: Float,
    maxEnergy: Float,
    modifier: Modifier = Modifier
) {
    val percent = if (maxEnergy == 0f) 0f else (energy / maxEnergy).coerceIn(0f, 1f)
    val barColor = when {
        percent > 0.6f -> EnergyFull
        percent > 0.3f -> EnergyMid
        else -> EnergyLow
    }
    Column(modifier = modifier) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⚡", fontSize = 14.sp)
                Spacer(Modifier.width(4.dp))
                Text("Energy", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Text(
                "${energy.toInt()}/${maxEnergy.toInt()}",
                style = MaterialTheme.typography.bodySmall,
                color = barColor,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(4.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(DarkBorder, RoundedCornerShape(4.dp))
        ) {
            val animatedWidth by animateFloatAsState(percent, label = "energy")
            Box(
                Modifier
                    .fillMaxWidth(animatedWidth)
                    .fillMaxHeight()
                    .background(
                        Brush.horizontalGradient(listOf(barColor.copy(0.7f), barColor)),
                        RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}
