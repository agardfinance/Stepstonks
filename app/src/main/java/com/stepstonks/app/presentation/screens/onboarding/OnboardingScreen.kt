package com.stepstonks.app.presentation.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.stepstonks.app.presentation.theme.*

data class OnboardingPage(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val accentColor: Color
)

val ONBOARDING_PAGES = listOf(
    OnboardingPage("👟", "Walk & Earn", "Turn every step into crypto", "Stepstonks rewards you with STK tokens for every step you take. The more you walk, the more you earn!", NeonGreen),
    OnboardingPage("⚡", "NFT Sneakers", "Boost your earning power", "Equip NFT sneakers to multiply your earning rate. Upgrade them, mint new ones, and trade on the marketplace!", ElectricPurple),
    OnboardingPage("🏆", "Compete & Win", "Rise up the leaderboard", "Join daily and weekly challenges, earn achievement badges, and compete with walkers worldwide for massive token rewards!", GoldYellow),
    OnboardingPage("🔥", "Streak Bonuses", "Consistency pays off", "Maintain your daily walk streak to unlock exponential earning bonuses. 10 days = 50% more STK every step!", NeonOrange),
    OnboardingPage("💎", "Your Wallet", "Withdraw real earnings", "Your STK tokens have real value. Hold, stake, or withdraw to your crypto wallet. Your legs = your bank!", NeonBlue),
)

private val totalPages = ONBOARDING_PAGES.size + 1

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var currentPage by remember { mutableIntStateOf(0) }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var username by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                } else {
                    slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                }
            },
            modifier = Modifier.fillMaxSize(),
            label = "onboarding_page"
        ) { page ->
            if (page < ONBOARDING_PAGES.size) {
                OnboardingPageContent(ONBOARDING_PAGES[page])
            } else {
                CreateAccountPage(
                    username = username,
                    onUsernameChange = { username = it },
                    isLoading = isLoading,
                    error = error,
                    onCreateAccount = { viewModel.createAccount(username, onComplete) }
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(totalPages) { i ->
                    val isSelected = currentPage == i
                    Box(
                        Modifier
                            .size(if (isSelected) 24.dp else 8.dp, 8.dp)
                            .background(
                                if (isSelected) NeonGreen else DarkBorder,
                                RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            if (currentPage < ONBOARDING_PAGES.size) {
                Button(
                    onClick = { currentPage++ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Next", color = DarkBackground, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = { currentPage = ONBOARDING_PAGES.size }) {
                    Text("Skip", color = TextMuted)
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(listOf(page.accentColor.copy(0.2f), DarkBackground, DarkBackground))
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Text(page.emoji, fontSize = 80.sp)
            Spacer(Modifier.height(32.dp))
            Text(
                page.title,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = page.accentColor,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                page.subtitle,
                fontSize = 16.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Text(
                page.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CreateAccountPage(
    username: String,
    onUsernameChange: (String) -> Unit,
    isLoading: Boolean,
    error: String?,
    onCreateAccount: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.radialGradient(listOf(NeonGreen.copy(0.15f), DarkBackground))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text("🚀", fontSize = 64.sp)
            Spacer(Modifier.height(24.dp))
            Text("Create Your Account", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary, textAlign = TextAlign.Center)
            Text("Choose your walker name to get started", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
            Spacer(Modifier.height(32.dp))
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Username", color = TextMuted) },
                placeholder = { Text("e.g. CryptoWalker", color = TextMuted) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonGreen,
                    unfocusedBorderColor = DarkBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = NeonGreen
                ),
                shape = RoundedCornerShape(12.dp),
                isError = error != null
            )
            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Text(error, color = CrimsonRed, fontSize = 12.sp)
            }
            Spacer(Modifier.height(8.dp))
            Text("You'll receive a FREE Common Sneaker + 10 STK to start!", fontSize = 12.sp, color = GoldYellow, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onCreateAccount,
                enabled = username.length >= 3 && !isLoading,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) CircularProgressIndicator(Modifier.size(24.dp), color = DarkBackground)
                else Text("Start Walking & Earning!", color = DarkBackground, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
