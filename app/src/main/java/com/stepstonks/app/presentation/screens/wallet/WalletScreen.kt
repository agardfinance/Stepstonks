package com.stepstonks.app.presentation.screens.wallet

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stepstonks.app.domain.model.*
import com.stepstonks.app.presentation.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WalletScreen(viewModel: WalletViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredAssets by viewModel.filteredAssets.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showWithdrawDialog by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().background(DarkBackground)) {

        // ── Hero header ──────────────────────────────────────────────────
        Box(
            Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(GoldYellow.copy(0.3f), ElectricPurple.copy(0.2f), DarkBackground)))
                .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                val stkValue = (state.user?.tokenBalance ?: 0.0) * state.stkPrice
                val totalUsd = stkValue  // extend with other holdings when available
                Text("Total Portfolio Value", fontSize = 13.sp, color = TextSecondary)
                Spacer(Modifier.height(4.dp))
                Text(
                    "$%,.2f".format(totalUsd),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                val stkChange = state.stkPriceChange
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(
                        if (stkChange >= 0) Icons.Filled.TrendingUp else Icons.Filled.TrendingDown,
                        contentDescription = null,
                        tint = if (stkChange >= 0) NeonGreen else CrimsonRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "%+.2f%% (24h)".format(stkChange),
                        fontSize = 14.sp,
                        color = if (stkChange >= 0) NeonGreen else CrimsonRed,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { showWithdrawDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(0.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Brush.linearGradient(listOf(GoldYellow, GoldDim)))
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Filled.ArrowUpward, null, modifier = Modifier.size(16.dp), tint = DarkBackground)
                                Text("Withdraw", color = DarkBackground, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = {},
                        border = BorderStroke(1.dp, NeonGreen.copy(0.6f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Filled.ArrowDownward, null, modifier = Modifier.size(16.dp), tint = NeonGreen)
                        Spacer(Modifier.width(4.dp))
                        Text("Deposit", color = NeonGreen)
                    }
                }
            }
        }

        // ── Tabs ──────────────────────────────────────────────────────────
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DarkSurface,
            contentColor = GoldYellow,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(2.dp)
                        .background(Brush.horizontalGradient(listOf(GoldYellow, GoldDim)))
                )
            }
        ) {
            listOf("💼 Portfolio", "📊 Market").forEachIndexed { i, label ->
                Tab(
                    selected = selectedTab == i,
                    onClick = { selectedTab = i },
                    text = {
                        Text(
                            label,
                            color = if (selectedTab == i) GoldYellow else TextMuted,
                            fontWeight = if (selectedTab == i) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        // ── Tab content ───────────────────────────────────────────────────
        when (selectedTab) {
            0 -> PortfolioTab(state, viewModel)
            1 -> MarketTab(state, filteredAssets, viewModel)
        }
    }

    if (showWithdrawDialog) {
        WithdrawDialog(balance = state.user?.tokenBalance ?: 0.0, stkPrice = state.stkPrice) {
            showWithdrawDialog = false
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Portfolio tab
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun PortfolioTab(state: WalletUiState, viewModel: WalletViewModel) {
    val user = state.user
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // STK asset card
        item {
            STKAssetCard(
                balance = user?.tokenBalance ?: 0.0,
                price = state.stkPrice,
                change = state.stkPriceChange
            )
        }

        // Stats row
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MiniStatCard("📈", "Total Earned", "%.0f STK".format(state.totalEarned), NeonGreen, Modifier.weight(1f))
                MiniStatCard("📉", "Total Spent", "%.0f STK".format(state.totalSpent), CrimsonRed, Modifier.weight(1f))
            }
        }

        // Transactions header
        item {
            Text("Recent Transactions", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
        }

        if (state.recentTransactions.isEmpty()) {
            item {
                Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💸", fontSize = 40.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("No transactions yet", color = TextMuted)
                        Text("Start walking to earn STK!", fontSize = 12.sp, color = TextMuted)
                    }
                }
            }
        } else {
            items(state.recentTransactions) { tx -> TransactionRow(tx) }
        }

        // Referral
        item {
            user?.let {
                ReferralCard(referralCode = it.referralCode, referralCount = it.referralCount)
            }
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun STKAssetCard(balance: Double, price: Double, change: Double) {
    Box(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(GoldYellow.copy(0.25f), GoldDim.copy(0.1f))))
            .border(1.dp, GoldYellow.copy(0.5f), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    Modifier.size(48.dp).clip(CircleShape).background(GoldYellow.copy(0.3f)).border(2.dp, GoldYellow.copy(0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Text("👟", fontSize = 22.sp) }
                Column {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("STEPSTONKS", fontWeight = FontWeight.ExtraBold, color = GoldYellow, fontSize = 14.sp)
                        Box(
                            Modifier.clip(RoundedCornerShape(4.dp)).background(GoldYellow.copy(0.2f)).padding(horizontal = 4.dp, vertical = 1.dp)
                        ) { Text("STK", fontSize = 9.sp, color = GoldYellow, fontWeight = FontWeight.Bold) }
                    }
                    Text("%,.2f STK".format(balance), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("$%.4f".format(price), fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                val usdValue = balance * price
                Text("≈ $%.2f".format(usdValue), fontSize = 12.sp, color = TextSecondary)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Icon(
                        if (change >= 0) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        null, tint = if (change >= 0) NeonGreen else CrimsonRed, modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "%+.2f%%".format(change), fontSize = 12.sp,
                        color = if (change >= 0) NeonGreen else CrimsonRed, fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniStatCard(emoji: String, label: String, value: String, color: Color, modifier: Modifier) {
    Box(
        modifier.clip(RoundedCornerShape(14.dp)).background(color.copy(0.1f)).border(1.dp, color.copy(0.3f), RoundedCornerShape(14.dp)).padding(16.dp)
    ) {
        Column {
            Text(emoji, fontSize = 18.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Bold, color = color, fontSize = 15.sp)
            Text(label, fontSize = 10.sp, color = TextMuted)
        }
    }
}

@Composable
private fun TransactionRow(transaction: TokenTransaction) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(DarkCard).border(1.dp, DarkBorder, RoundedCornerShape(12.dp)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(40.dp).clip(CircleShape).background(
                if (transaction.isEarning) NeonGreen.copy(0.15f) else CrimsonRed.copy(0.15f)
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(if (transaction.isEarning) "💰" else "🛍️", fontSize = 18.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(transaction.description, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(SimpleDateFormat("MMM dd, HH:mm", Locale.US).format(Date(transaction.timestamp)), fontSize = 11.sp, color = TextMuted)
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
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .background(Brush.linearGradient(listOf(ElectricPurple.copy(0.2f), NeonGreen.copy(0.1f))))
            .border(1.dp, ElectricPurple.copy(0.3f), RoundedCornerShape(16.dp)).padding(20.dp)
    ) {
        Column {
            Text("🎁 Referral Program", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
            Spacer(Modifier.height(4.dp))
            Text("Invite friends — earn 50 STK per signup!", fontSize = 13.sp, color = TextSecondary)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
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

// ─────────────────────────────────────────────────────────────────────────────
// Market tab
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun MarketTab(state: WalletUiState, assets: List<CryptoAsset>, viewModel: WalletViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Search + refresh header
        item {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = viewModel::setSearchQuery,
                    modifier = Modifier.weight(1f).height(52.dp),
                    placeholder = { Text("Search crypto...", color = TextMuted, fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Filled.Search, null, tint = TextMuted, modifier = Modifier.size(18.dp)) },
                    trailingIcon = if (state.searchQuery.isNotEmpty()) {
                        { IconButton(onClick = { viewModel.setSearchQuery("") }) { Icon(Icons.Filled.Clear, null, tint = TextMuted, modifier = Modifier.size(16.dp)) } }
                    } else null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldYellow.copy(0.6f),
                        unfocusedBorderColor = DarkBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = GoldYellow
                    ),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                IconButton(
                    onClick = viewModel::refreshPrices,
                    modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp)).background(DarkCard).border(1.dp, DarkBorder, RoundedCornerShape(14.dp))
                ) {
                    AnimatedContent(targetState = state.isLoadingCrypto, label = "refresh") { loading ->
                        if (loading) CircularProgressIndicator(Modifier.size(20.dp), color = GoldYellow, strokeWidth = 2.dp)
                        else Icon(Icons.Filled.Refresh, null, tint = GoldYellow, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        // Last updated
        item {
            if (state.lastUpdated > 0) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (state.cryptoError) "⚠ Offline prices" else "🟢 Live prices",
                        fontSize = 11.sp, color = if (state.cryptoError) NeonOrange else NeonGreen
                    )
                    Text(
                        "Updated ${timeAgoShort(state.lastUpdated)}",
                        fontSize = 11.sp, color = TextMuted
                    )
                }
                Spacer(Modifier.height(4.dp))
            }
        }

        // Column headers
        item {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Asset", fontSize = 11.sp, color = TextMuted, modifier = Modifier.weight(2f))
                Text("Price", fontSize = 11.sp, color = TextMuted, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                Text("24h", fontSize = 11.sp, color = TextMuted, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                Text("7d Chart", fontSize = 11.sp, color = TextMuted, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
            }
            HorizontalDivider(color = DarkBorder.copy(0.5f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 12.dp))
        }

        // STK always at top (pinned)
        if (state.searchQuery.isBlank() || "stepstonks".contains(state.searchQuery, ignoreCase = true) || "stk".contains(state.searchQuery, ignoreCase = true)) {
            item {
                STKMarketRow(price = state.stkPrice, change = state.stkPriceChange)
                HorizontalDivider(color = DarkBorder.copy(0.3f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 12.dp))
            }
        }

        // Loading shimmer
        if (state.isLoadingCrypto) {
            items(10) { ShimmerCryptoRow() }
        } else {
            items(assets, key = { it.id }) { asset ->
                CryptoMarketRow(asset)
                HorizontalDivider(color = DarkBorder.copy(0.3f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 12.dp))
            }
        }
    }
}

@Composable
private fun STKMarketRow(price: Double, change: Double) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(GoldYellow.copy(0.05f))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon + name
        Row(Modifier.weight(2f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                Modifier.size(38.dp).clip(CircleShape).background(GoldYellow.copy(0.25f)).border(1.dp, GoldYellow.copy(0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) { Text("👟", fontSize = 18.sp) }
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("STK", fontWeight = FontWeight.ExtraBold, color = GoldYellow, fontSize = 13.sp)
                    Box(
                        Modifier.clip(RoundedCornerShape(3.dp)).background(GoldYellow.copy(0.2f)).padding(horizontal = 3.dp, vertical = 1.dp)
                    ) { Text("EARN", fontSize = 8.sp, color = GoldYellow) }
                }
                Text("Stepstonks", fontSize = 11.sp, color = TextMuted)
            }
        }
        // Price
        Text("$%.4f".format(price), modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 13.sp)
        // 24h
        Text(
            "%+.2f%%".format(change),
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            color = if (change >= 0) NeonGreen else CrimsonRed,
            fontWeight = FontWeight.SemiBold
        )
        // Flat chart (no real historical data for STK)
        Box(Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            Box(Modifier.height(28.dp).width(60.dp)) {
                FlatSparkline(change >= 0)
            }
        }
    }
}

@Composable
private fun CryptoMarketRow(asset: CryptoAsset) {
    Row(
        Modifier.fillMaxWidth().clickable {}.padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon + name
        Row(Modifier.weight(2f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CoinAvatar(asset.symbol, 38.dp)
            Column {
                Text(asset.symbol, fontWeight = FontWeight.ExtraBold, color = TextPrimary, fontSize = 13.sp)
                Text(asset.name, fontSize = 11.sp, color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        // Price
        AnimatedContent(targetState = asset.priceFormatted, transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "price_${asset.id}") { price ->
            Text(price, modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontWeight = FontWeight.SemiBold, color = TextPrimary, fontSize = 13.sp)
        }
        // 24h change
        Text(
            asset.changeFormatted,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            color = if (asset.isPositive) NeonGreen else CrimsonRed,
            fontWeight = FontWeight.SemiBold
        )
        // Sparkline
        Box(Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
            Box(Modifier.height(28.dp).width(60.dp)) {
                if (asset.sparkline.size >= 2) {
                    MiniSparkline(data = asset.sparkline, isPositive = asset.isPositive)
                } else {
                    FlatSparkline(asset.isPositive)
                }
            }
        }
    }
}

@Composable
private fun CoinAvatar(symbol: String, size: Dp) {
    val colorPairs = mapOf(
        "BTC" to Pair(Color(0xFFF7931A), Color(0xFFFF9800)),
        "ETH" to Pair(Color(0xFF627EEA), Color(0xFF3C4FBF)),
        "BNB" to Pair(Color(0xFFF3BA2F), Color(0xFFE6A817)),
        "SOL" to Pair(Color(0xFF9945FF), Color(0xFF14F195)),
        "XRP" to Pair(Color(0xFF00AAE4), Color(0xFF0077B6)),
        "ADA" to Pair(Color(0xFF0033AD), Color(0xFF2A5FDE)),
        "AVAX" to Pair(Color(0xFFE84142), Color(0xFFCC2929)),
        "DOGE" to Pair(Color(0xFFC2A633), Color(0xFF9E862A)),
        "DOT" to Pair(Color(0xFFE6007A), Color(0xFFBF005E)),
        "MATIC" to Pair(Color(0xFF8247E5), Color(0xFF6A35C9)),
        "LINK" to Pair(Color(0xFF2A5ADA), Color(0xFF1A3FA8)),
        "ATOM" to Pair(Color(0xFF2E3148), Color(0xFF4B4F7A)),
        "UNI" to Pair(Color(0xFFFF007A), Color(0xFFCC005E)),
        "LTC" to Pair(Color(0xFF345D9D), Color(0xFF2A4A80)),
        "TON" to Pair(Color(0xFF0098EA), Color(0xFF0077B8)),
        "TRX" to Pair(Color(0xFFEF0027), Color(0xFFBF001F)),
    )
    val (c1, c2) = colorPairs[symbol] ?: Pair(ElectricPurple, NeonBlue)
    val text = cryptoEmoji(symbol)

    Box(
        Modifier.size(size).clip(CircleShape).background(Brush.linearGradient(listOf(c1, c2))),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = (size.value * 0.42f).sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
    }
}

@Composable
private fun MiniSparkline(data: List<Double>, isPositive: Boolean) {
    val color = if (isPositive) NeonGreen else CrimsonRed
    androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val min = data.min()
        val max = data.max()
        val range = (max - min).let { if (it == 0.0) 1.0 else it }
        val points = data.mapIndexed { i, v ->
            Offset(
                x = i.toFloat() / (data.size - 1) * w,
                y = h - ((v - min) / range * h).toFloat()
            )
        }
        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            points.drop(1).forEach { lineTo(it.x, it.y) }
        }
        drawPath(path, color = color.copy(alpha = 0.25f), style = Stroke(width = 1.dp.toPx()))

        // Gradient fill
        val fillPath = Path().apply {
            moveTo(0f, h)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(w, h)
            close()
        }
        drawPath(fillPath, brush = Brush.verticalGradient(listOf(color.copy(0.3f), Color.Transparent)))
        drawPath(path, color = color, style = Stroke(width = 1.5.dp.toPx()))
    }
}

@Composable
private fun FlatSparkline(isPositive: Boolean) {
    val color = if (isPositive) NeonGreen else CrimsonRed
    androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
        drawLine(color.copy(0.5f), Offset(0f, size.height / 2), Offset(size.width, size.height / 2), strokeWidth = 1.5.dp.toPx())
    }
}

@Composable
private fun ShimmerCryptoRow() {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by transition.animateFloat(0.2f, 0.6f, infiniteRepeatable(tween(900), RepeatMode.Reverse), label = "a")
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(Modifier.size(38.dp).clip(CircleShape).background(DarkCard.copy(alpha)))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(Modifier.width(60.dp).height(12.dp).clip(RoundedCornerShape(4.dp)).background(DarkCard.copy(alpha)))
            Box(Modifier.width(90.dp).height(10.dp).clip(RoundedCornerShape(4.dp)).background(DarkCard.copy(alpha)))
        }
        Box(Modifier.width(70.dp).height(14.dp).clip(RoundedCornerShape(4.dp)).background(DarkCard.copy(alpha)))
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Withdraw dialog
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun WithdrawDialog(balance: Double, stkPrice: Double, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkCard,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("💰", fontSize = 24.sp)
                Text("Withdraw STK", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(GoldYellow.copy(0.1f)).border(1.dp, GoldYellow.copy(0.3f), RoundedCornerShape(12.dp)).padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Balance", color = TextMuted)
                    Column(horizontalAlignment = Alignment.End) {
                        Text("%,.2f STK".format(balance), color = GoldYellow, fontWeight = FontWeight.Bold)
                        Text("≈ $%.2f USD".format(balance * stkPrice), color = TextSecondary, fontSize = 12.sp)
                    }
                }
                Text("Minimum withdrawal: 100 STK", fontSize = 12.sp, color = TextMuted)
                Box(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(NeonGreen.copy(0.1f)).border(1.dp, NeonGreen.copy(0.3f), RoundedCornerShape(12.dp)).padding(12.dp)
                ) {
                    Text("🔗 Connect your external crypto wallet to withdraw STK to any EVM address.", fontSize = 12.sp, color = NeonGreen)
                }
                Text("Network fees apply. Processing time: 24–48h.", fontSize = 11.sp, color = TextMuted)
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    Modifier.clip(RoundedCornerShape(12.dp)).background(Brush.linearGradient(listOf(GoldYellow, GoldDim))).padding(horizontal = 24.dp, vertical = 10.dp)
                ) { Text("Withdraw", color = DarkBackground, fontWeight = FontWeight.Bold) }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextMuted) }
        }
    )
}

private fun timeAgoShort(ts: Long): String {
    val diff = System.currentTimeMillis() - ts
    return when {
        diff < 60_000L -> "just now"
        diff < 3_600_000L -> "${diff / 60_000}m ago"
        else -> "${diff / 3_600_000}h ago"
    }
}
