package com.stepstonks.app.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.stepstonks.app.presentation.screens.achievements.AchievementsScreen
import com.stepstonks.app.presentation.screens.challenges.ChallengesScreen
import com.stepstonks.app.presentation.screens.home.HomeScreen
import com.stepstonks.app.presentation.screens.leaderboard.LeaderboardScreen
import com.stepstonks.app.presentation.screens.onboarding.OnboardingScreen
import com.stepstonks.app.presentation.screens.profile.ProfileScreen
import com.stepstonks.app.presentation.screens.sneakers.SneakersScreen
import com.stepstonks.app.presentation.screens.splash.SplashScreen
import com.stepstonks.app.presentation.screens.walk.WalkScreen
import com.stepstonks.app.presentation.screens.wallet.WalletScreen
import com.stepstonks.app.presentation.theme.*

data class NavItem(val label: String, val icon: ImageVector, val screen: Screen)

val bottomNavItems = listOf(
    NavItem("Home", Icons.Filled.Home, Screen.Home),
    NavItem("Walk", Icons.Filled.DirectionsWalk, Screen.Walk),
    NavItem("Challenges", Icons.Filled.EmojiEvents, Screen.Challenges),
    NavItem("Leaderboard", Icons.Filled.Leaderboard, Screen.Leaderboard),
    NavItem("Profile", Icons.Filled.Person, Screen.Profile),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepstonksNavGraph(startDestination: String = Screen.Splash.route) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomNavItems.map { it.screen.route }

    Scaffold(
        containerColor = DarkBackground,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = DarkCard,
                    contentColor = NeonGreen,
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(item.icon, contentDescription = item.label)
                            },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonGreen,
                                selectedTextColor = NeonGreen,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted,
                                indicatorColor = DarkCardElevated
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn() + slideInHorizontally { it / 4 } },
            exitTransition = { fadeOut() + slideOutHorizontally { -it / 4 } },
            popEnterTransition = { fadeIn() + slideInHorizontally { -it / 4 } },
            popExitTransition = { fadeOut() + slideOutHorizontally { it / 4 } }
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateToOnboarding = {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onComplete = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToWalk = { navController.navigate(Screen.Walk.route) },
                    onNavigateToChallenges = { navController.navigate(Screen.Challenges.route) },
                    onNavigateToWallet = { navController.navigate(Screen.Wallet.route) },
                    onNavigateToSneakers = { navController.navigate(Screen.Sneakers.route) }
                )
            }
            composable(Screen.Walk.route) { WalkScreen() }
            composable(Screen.Challenges.route) {
                ChallengesScreen(
                    onNavigateToAchievements = { navController.navigate(Screen.Achievements.route) }
                )
            }
            composable(Screen.Achievements.route) { AchievementsScreen() }
            composable(Screen.Leaderboard.route) { LeaderboardScreen() }
            composable(Screen.Wallet.route) { WalletScreen() }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToSneakers = { navController.navigate(Screen.Sneakers.route) },
                    onNavigateToAchievements = { navController.navigate(Screen.Achievements.route) },
                    onNavigateToWallet = { navController.navigate(Screen.Wallet.route) }
                )
            }
            composable(Screen.Sneakers.route) {
                SneakersScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
