package com.stepstonks.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.stepstonks.app.presentation.theme.*

@Composable
fun GlowingCard(
    modifier: Modifier = Modifier,
    glowColor: Color = NeonGreen,
    cornerRadius: Dp = 16.dp,
    glowRadius: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = glowRadius,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = glowColor.copy(alpha = 0.3f),
                spotColor = glowColor.copy(alpha = 0.4f)
            )
            .background(DarkCard, RoundedCornerShape(cornerRadius))
            .border(1.dp, glowColor.copy(alpha = 0.2f), RoundedCornerShape(cornerRadius))
    ) {
        content()
    }
}

@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(ElectricPurple.copy(alpha = 0.3f), NeonGreen.copy(alpha = 0.1f)),
    cornerRadius: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                Brush.linearGradient(colors),
                RoundedCornerShape(cornerRadius)
            )
            .border(1.dp, colors.first().copy(alpha = 0.5f), RoundedCornerShape(cornerRadius))
    ) {
        content()
    }
}
