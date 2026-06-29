package com.stepstonks.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.stepstonks.app.domain.model.Challenge
import com.stepstonks.app.domain.model.ChallengeStatus
import com.stepstonks.app.presentation.theme.*

@Composable
fun ChallengeCard(
    challenge: Challenge,
    onClaim: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCompleted = challenge.status == ChallengeStatus.COMPLETED
    val isClaimed = challenge.status == ChallengeStatus.CLAIMED
    val borderColor = when {
        isClaimed -> TextMuted
        isCompleted -> GoldYellow
        else -> DarkBorder
    }
    val progress by animateFloatAsState(challenge.progressPercent, label = "cp")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkCard, RoundedCornerShape(16.dp))
            .border(1.dp, borderColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Text(challenge.iconEmoji, fontSize = 28.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            challenge.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (isClaimed) TextMuted else TextPrimary
                        )
                        Text(
                            challenge.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.End) {
                    if (challenge.tokenReward > 0) {
                        Text(
                            "+${challenge.tokenReward.toInt()} STK",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldYellow
                        )
                    }
                    if (challenge.xpReward > 0) {
                        Text(
                            "+${challenge.xpReward} XP",
                            fontSize = 11.sp,
                            color = ElectricPurple
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(DarkBorder, RoundedCornerShape(3.dp))
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(
                            Brush.horizontalGradient(
                                if (isCompleted || isClaimed)
                                    listOf(GoldYellow.copy(0.7f), GoldYellow)
                                else
                                    listOf(NeonGreen.copy(0.7f), NeonGreen)
                            ),
                            RoundedCornerShape(3.dp)
                        )
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${challenge.currentValue} / ${challenge.targetValue}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                when {
                    isClaimed -> Text("✅ Claimed", fontSize = 12.sp, color = TextMuted)
                    isCompleted -> Button(
                        onClick = onClaim,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldYellow),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Claim Reward", fontSize = 12.sp, color = DarkBackground, fontWeight = FontWeight.Bold)
                    }
                    else -> Text(
                        "${(challenge.progressPercent * 100).toInt()}%",
                        fontSize = 12.sp,
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
