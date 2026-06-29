package com.stepstonks.app.presentation.screens.sneakers

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stepstonks.app.domain.model.Sneaker
import com.stepstonks.app.domain.model.SneakerRarity
import com.stepstonks.app.presentation.theme.*

@Composable
fun SneakersScreen(
    onBack: () -> Unit,
    viewModel: SneakersViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.actionMessage) {
        if (state.actionMessage != null) {
            kotlinx.coroutines.delay(2000)
            viewModel.clearMessage()
        }
    }

    Box(Modifier.fillMaxSize().background(DarkBackground)) {
        Column(Modifier.fillMaxSize()) {
            // Header
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(ElectricPurple.copy(0.3f), DarkBackground)))
                    .padding(top = 48.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("👟 Sneakers", style = MaterialTheme.typography.headlineSmall, color = TextPrimary)
                        Text("Balance: ${"%,.0f".format(state.userTokenBalance)} STK", fontSize = 12.sp, color = GoldYellow)
                    }
                }
            }

            // Tabs
            TabRow(
                selectedTabIndex = state.selectedTab,
                containerColor = DarkSurface,
                contentColor = NeonGreen
            ) {
                Tab(selected = state.selectedTab == 0, onClick = { viewModel.selectTab(0) }) {
                    Text("My Sneakers (${state.mySneakers.size})", modifier = Modifier.padding(12.dp), color = if (state.selectedTab == 0) NeonGreen else TextMuted)
                }
                Tab(selected = state.selectedTab == 1, onClick = { viewModel.selectTab(1) }) {
                    Text("Shop", modifier = Modifier.padding(12.dp), color = if (state.selectedTab == 1) NeonGreen else TextMuted)
                }
            }

            // Content
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state.selectedTab == 0) {
                    if (state.mySneakers.isEmpty()) {
                        EmptyState("👟", "No sneakers yet", "Visit the Shop to get your first NFT sneaker!")
                    } else {
                        state.mySneakers.forEach { sneaker ->
                            SneakerCard(
                                sneaker = sneaker,
                                isEquipped = sneaker.id == state.equippedSneakerId,
                                onEquip = { viewModel.equipSneaker(sneaker.id) },
                                onUpgrade = { viewModel.upgradeSneaker(sneaker.id) },
                                showBuyButton = false
                            )
                        }
                    }
                } else {
                    Text("⚡ NFT Sneaker Marketplace", fontWeight = FontWeight.Bold, color = GoldYellow, fontSize = 16.sp)
                    Text("Equip better sneakers to multiply your earnings!", fontSize = 12.sp, color = TextSecondary)
                    Spacer(Modifier.height(4.dp))
                    state.shopItems.forEach { sneaker ->
                        SneakerCard(
                            sneaker = sneaker,
                            isEquipped = false,
                            onBuy = { viewModel.buySneaker(sneaker) },
                            showBuyButton = true
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }

        // Action message toast
        state.actionMessage?.let { msg ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                containerColor = DarkCard
            ) {
                Text(msg, color = NeonGreen)
            }
        }
    }
}

@Composable
fun SneakerCard(
    sneaker: Sneaker,
    isEquipped: Boolean,
    onEquip: (() -> Unit)? = null,
    onUpgrade: (() -> Unit)? = null,
    onBuy: (() -> Unit)? = null,
    showBuyButton: Boolean = false,
    modifier: Modifier = Modifier
) {
    val rarityColor = try {
        Color(android.graphics.Color.parseColor(sneaker.rarity.colorHex))
    } catch (e: Exception) {
        NeonGreen
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(rarityColor.copy(0.15f), DarkCard)),
                RoundedCornerShape(16.dp)
            )
            .border(
                if (isEquipped) 2.dp else 1.dp,
                if (isEquipped) rarityColor else rarityColor.copy(0.3f),
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("👟", fontSize = 40.sp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(sneaker.name, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                            if (isEquipped) {
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "EQUIPPED",
                                    fontSize = 9.sp,
                                    color = NeonGreen,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    modifier = Modifier
                                        .background(NeonGreen.copy(0.15f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            RarityBadge(sneaker.rarity, rarityColor)
                            Text(sneaker.type.displayName, fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Lv.${sneaker.level}", fontWeight = FontWeight.Bold, color = GoldYellow, fontSize = 14.sp)
                    Text("${sneaker.rarity.earningMultiplier}x", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = rarityColor)
                    Text("multiplier", fontSize = 10.sp, color = TextMuted)
                }
            }

            Spacer(Modifier.height(12.dp))

            // Attributes
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                AttributeStat("⚡ Eff", sneaker.attributes.efficiency)
                AttributeStat("🍀 Luck", sneaker.attributes.luck)
                AttributeStat("💨 Cmf", sneaker.attributes.comfort)
                AttributeStat("🛡️ Res", sneaker.attributes.resilience)
            }

            Spacer(Modifier.height(8.dp))

            // Durability bar
            if (!showBuyButton) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Durability", fontSize = 11.sp, color = TextMuted)
                    Text("${sneaker.durability.toInt()}%", fontSize = 11.sp, color = if (sneaker.durability > 30) NeonGreen else CrimsonRed)
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(DarkBorder, RoundedCornerShape(2.dp))
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(sneaker.durabilityPercent)
                            .fillMaxHeight()
                            .background(
                                if (sneaker.durability > 50) NeonGreen else if (sneaker.durability > 30) GoldYellow else CrimsonRed,
                                RoundedCornerShape(2.dp)
                            )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (showBuyButton && onBuy != null) {
                    Button(
                        onClick = onBuy,
                        colors = ButtonDefaults.buttonColors(containerColor = rarityColor),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            if (sneaker.rarity.mintCost == 0.0) "FREE (Starter)" else "${sneaker.rarity.mintCost.toInt()} STK",
                            color = DarkBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    if (!isEquipped && onEquip != null) {
                        OutlinedButton(
                            onClick = onEquip,
                            border = BorderStroke(1.dp, rarityColor.copy(0.5f)),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Equip", color = rarityColor, fontSize = 13.sp)
                        }
                    }
                    if (onUpgrade != null) {
                        Button(
                            onClick = onUpgrade,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldYellow),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Upgrade • ${(sneaker.level + 1) * 50} STK", color = DarkBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RarityBadge(rarity: SneakerRarity, color: Color) {
    Text(
        rarity.displayName,
        fontSize = 10.sp,
        color = color,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(color.copy(0.15f), RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
private fun AttributeStat(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 10.sp, color = TextMuted)
        Text("$value", fontWeight = FontWeight.Bold, color = NeonGreen, fontSize = 14.sp)
    }
}

@Composable
private fun EmptyState(emoji: String, title: String, subtitle: String) {
    Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(emoji, fontSize = 48.sp)
            Spacer(Modifier.height(8.dp))
            Text(title, color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = TextMuted, fontSize = 12.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}
