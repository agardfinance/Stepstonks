package com.stepstonks.app.presentation.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.stepstonks.app.presentation.theme.*

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val destination by viewModel.destination.collectAsState()

    LaunchedEffect(destination) {
        when (destination) {
            SplashDestination.HOME -> onNavigateToHome()
            SplashDestination.ONBOARDING -> onNavigateToOnboarding()
            null -> {}
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f, targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "scale"
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "glow"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.radialGradient(listOf(ElectricPurple.copy(0.3f), DarkBackground))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "👟",
                fontSize = 80.sp,
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "STEPSTONKS",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = NeonGreen,
                letterSpacing = 4.sp
            )
            Text(
                "Walk. Earn. Prosper.",
                fontSize = 14.sp,
                color = TextSecondary,
                letterSpacing = 2.sp
            )
        }
    }
}

