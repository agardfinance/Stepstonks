package com.stepstonks.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = DarkBackground,
    primaryContainer = ElectricPurple,
    onPrimaryContainer = TextPrimary,
    secondary = ElectricPurple,
    onSecondary = TextPrimary,
    secondaryContainer = DarkCard,
    onSecondaryContainer = TextSecondary,
    tertiary = GoldYellow,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondary,
    outline = DarkBorder,
    error = CrimsonRed,
    onError = TextPrimary
)

@Composable
fun StepstonksTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = StepstonksTypography,
        content = content
    )
}
