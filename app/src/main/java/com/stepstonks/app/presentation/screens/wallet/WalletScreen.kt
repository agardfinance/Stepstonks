package com.stepstonks.app.presentation.screens.wallet

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stepstonks.app.domain.model.TokenTransaction
import com.stepstonks.app.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WalletScreen(viewModel: WalletViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showWithdrawDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
    ) {
        // Header with balance
        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(GoldYellow.copy(0.25f), ElectricPurple.copy(0.15f), DarkBackground))
                )
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 32.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("💎", fontSize = 48.sp)
                Spacer(Modifier.height(8.dp))
                Text("STEPSTONKS Wallet", style = MaterialTheme.typography.headlineSmall, color = TextSecondary)
                Spacer(Modifier.height(4.dp))
                Text(
                    "%,.2f STK".format(state.user?.tokenBalance ?: 0.0),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldYellow
                )
                state.user?.gemBalance?.let { gems ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("💎 $gems Gems", fontSize = 14.sp, color = NeonBlue)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { showWithdrawDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldYellow),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.ArrowUpward, contentDescription = null, modifier = Modifier.size(18.dp), tint = DarkBackground)
                        Spacer(Modifier.width(4.dp))
                        Text("Withdraw", color = DarkBackground, fontWeight = FontWeight.Bold)
                    }
                    OutlinedButton(
                        onClick = {},
                        border = BorderStroke(1.dp, NeonGreen.copy(0.5f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.ArrowDownward, contentDescription = null, modifier = Modifier.size(18.dp), tint = NeonGreen)
                        Spacer(Modifier.width(4.dp))
                        Text("Deposit", color = NeonGreen)
                    }
                }
            }
        }

        Column(Modifier.padding(16.dp)) {
            // Stats row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                WalletStatCard(
                    label = "Total Earned",
                    value = "%.0f STK".format(state.totalEarned),
                    icon = "📈",
                    color = NeonGreen,
                    modifier = Modifier.weight(1f)
                )
                WalletStatCard(
                    label = "Total Spent",
                    value = "%.0f STK".format(state.totalSpent),
                    icon = "📉",
                    color = CrimsonRed,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Recent transactions
            Text("Recent Transactions", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Spacer(Modifier.height(12.dp))

            if (state.recentTransactions.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💸", fontSize = 40.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("No transactions yet", color = TextMuted)
                        Text("Start walking to earn STK!", fontSize = 12.sp, color = TextMuted)
                    }
                }
            } else {
                state.recentTransactions.forEach { tx ->
                    TransactionItem(transaction = tx)
                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(24.dp))

            // Referral section
            state.user?.let { user ->
                ReferralCard(
                    referralCode = user.referralCode,
                    referralCount = user.referralCount
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    if (showWithdrawDialog) {
        WithdrawDialog(
            balance = state.user?.tokenBalance ?: 0.0,
            onDismiss = { showWithdrawDialog = false }
        )
    }
}

@Composable
private fun WalletStatCard(label: String, value: String, icon: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color.copy(0.1f), RoundedCornerShape(12.dp))
            .border(1.dp, color.copy(0.3f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(icon, fontSize = 20.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Bold, color = color, fontSize = 16.sp)
            Text(label, fontSize = 11.sp, color = TextMuted)
        }
    }
}

@Composable
private fun TransactionItem(transaction: TokenTransaction) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(DarkCard, RoundedCornerShape(12.dp))
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val emoji = when {
            transaction.isEarning -> "💰"
            transaction.isSpending -> "🛍️"
            else -> "💸"
        }
        Text(emoji, fontSize = 24.sp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(transaction.description, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
            Text(
                SimpleDateFormat("MMM dd, HH:mm", Locale.US).format(Date(transaction.timestamp)),
                fontSize = 11.sp,
                color = TextMuted
            )
        }
        Text(
            transaction.displayAmount,
            fontWeight = FontWeight.Bold,
            color = if (transaction.isEarning) NeonGreen else CrimsonRed,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ReferralCard(referralCode: String, referralCount: Int) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(ElectricPurple.copy(0.2f), NeonGreen.copy(0.1f))),
                RoundedCornerShape(16.dp)
            )
            .border(1.dp, ElectricPurple.copy(0.3f), RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column {
            Text("🎁 Referral Program", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            Text("Invite friends and earn 50 STK per signup!", fontSize = 13.sp, color = TextSecondary)
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Your Code", fontSize = 11.sp, color = TextMuted)
                    Text(referralCode, fontWeight = FontWeight.ExtraBold, color = GoldYellow, fontSize = 20.sp, letterSpacing = 2.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Friends Referred", fontSize = 11.sp, color = TextMuted)
                    Text("$referralCount", fontWeight = FontWeight.ExtraBold, color = NeonGreen, fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
private fun WithdrawDialog(balance: Double, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkCard,
        title = { Text("Withdraw STK", color = TextPrimary) },
        text = {
            Column {
                Text("Available: ${"%,.2f".format(balance)} STK", color = GoldYellow, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Minimum withdrawal: 100 STK", fontSize = 12.sp, color = TextMuted)
                Text("Connect your crypto wallet to withdraw.", fontSize = 12.sp, color = TextSecondary)
                Spacer(Modifier.height(8.dp))
                Text("🔗 Wallet connection coming soon!", fontSize = 13.sp, color = NeonGreen)
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = GoldYellow)
            ) {
                Text("OK", color = DarkBackground)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextMuted) }
        }
    )
}
