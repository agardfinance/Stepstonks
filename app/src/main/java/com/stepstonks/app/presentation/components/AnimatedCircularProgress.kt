package com.stepstonks.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.stepstonks.app.presentation.theme.*

@Composable
fun AnimatedCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    strokeWidth: Dp = 14.dp,
    trackColor: Color = DarkBorder,
    progressColor: Color = NeonGreen,
    glowColor: Color = NeonGreen,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val padding = strokePx / 2
            val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)
            val topLeft = Offset(padding, padding)

            // Background track
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(strokePx, cap = StrokeCap.Round)
            )

            // Glow layer
            if (animatedProgress > 0f) {
                drawArc(
                    color = glowColor.copy(alpha = glowAlpha * 0.3f),
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    topLeft = topLeft - Offset(4f, 4f),
                    size = arcSize + Size(8f, 8f),
                    style = Stroke(strokePx + 8f, cap = StrokeCap.Round)
                )
            }

            // Progress arc
            drawArc(
                brush = Brush.sweepGradient(
                    0f to progressColor.copy(alpha = 0.5f),
                    animatedProgress to progressColor
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(strokePx, cap = StrokeCap.Round)
            )
        }
        content()
    }
}

@Composable
fun StepProgressRing(
    steps: Long,
    goalSteps: Long,
    modifier: Modifier = Modifier
) {
    val progress = if (goalSteps == 0L) 0f else (steps.toFloat() / goalSteps.toFloat()).coerceIn(0f, 1f)
    AnimatedCircularProgress(
        progress = progress,
        modifier = modifier,
        size = 220.dp,
        strokeWidth = 16.dp,
        progressColor = if (progress >= 1f) GoldYellow else NeonGreen,
        glowColor = if (progress >= 1f) GoldYellow else NeonGreen
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "👟",
                fontSize = 32.sp
            )
            Text(
                text = "%,d".format(steps),
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (progress >= 1f) GoldYellow else TextPrimary
            )
            Text(
                text = "/ %,d".format(goalSteps),
                fontSize = 13.sp,
                color = TextSecondary
            )
            Text(
                text = "STEPS",
                fontSize = 11.sp,
                color = TextMuted,
                letterSpacing = 2.sp
            )
        }
    }
}
