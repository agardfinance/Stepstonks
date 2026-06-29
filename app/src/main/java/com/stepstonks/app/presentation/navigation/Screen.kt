package com.stepstonks.app.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Walk : Screen("walk")
    object Challenges : Screen("challenges")
    object Achievements : Screen("achievements")
    object Leaderboard : Screen("leaderboard")
    object Wallet : Screen("wallet")
    object Profile : Screen("profile")
    object Sneakers : Screen("sneakers")
}
