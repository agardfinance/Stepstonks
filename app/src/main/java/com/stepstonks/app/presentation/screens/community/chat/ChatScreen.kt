package com.stepstonks.app.presentation.screens.community.chat

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stepstonks.app.domain.model.ChatMessage
import com.stepstonks.app.domain.model.MessageType
import com.stepstonks.app.presentation.theme.*
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(viewModel: ChatViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Box(Modifier.fillMaxSize().background(DarkBackground)) {
        // Online count header
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(DarkSurface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                PulsingDot()
                Text("${state.onlineCount} online", fontSize = 12.sp, color = NeonGreen, fontWeight = FontWeight.SemiBold)
            }
            Text("Global Chat 🌐", fontSize = 13.sp, color = TextSecondary)
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 72.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(state.messages, key = { it.id }) { message ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + slideInVertically { it / 3 }
                ) {
                    MessageBubble(message = message, onReact = { emoji ->
                        viewModel.toggleReaction(message.id, emoji)
                    })
                }
            }
            if (state.isTyping) {
                item {
                    TypingIndicator()
                }
            }
        }

        // Input bar
        GlassInputBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .imePadding(),
            value = inputText,
            onValueChange = { inputText = it },
            onSend = {
                if (inputText.isNotBlank()) {
                    viewModel.sendMessage(inputText)
                    inputText = ""
                    scope.launch { listState.animateScrollToItem(state.messages.size) }
                }
            }
        )
    }
}

@Composable
private fun MessageBubble(message: ChatMessage, onReact: (String) -> Unit) {
    val isSystem = message.messageType == MessageType.ACHIEVEMENT || message.messageType == MessageType.CHALLENGE_COMPLETE

    if (isSystem) {
        SystemMessage(message)
        return
    }

    val alignment = if (message.isCurrentUser) Alignment.End else Alignment.Start
    val bubbleColor = when {
        message.isCurrentUser -> Brush.linearGradient(listOf(ElectricPurple, NeonBlue.copy(0.8f)))
        else -> Brush.linearGradient(listOf(DarkCard, DarkCardElevated))
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalAlignment = alignment
    ) {
        if (!message.isCurrentUser) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
            ) {
                AvatarCircle(message.username, size = 20.dp, fontSize = 10.sp)
                Text(message.username, fontSize = 11.sp, color = TextMuted, fontWeight = FontWeight.SemiBold)
            }
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp, topEnd = 16.dp,
                        bottomStart = if (message.isCurrentUser) 16.dp else 4.dp,
                        bottomEnd = if (message.isCurrentUser) 4.dp else 16.dp
                    )
                )
                .background(bubbleColor)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(message.content, color = TextPrimary, fontSize = 14.sp, lineHeight = 20.sp)
        }

        if (message.reactions.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 2.dp, start = if (message.isCurrentUser) 0.dp else 8.dp)
            ) {
                message.reactions.forEach { (emoji, count) ->
                    ReactionChip(emoji, count) { onReact(emoji) }
                }
            }
        }

        val quickReactions = listOf("🔥", "💪", "🚀", "❤️")
        if (!message.isCurrentUser) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(top = 2.dp, start = 8.dp)
            ) {
                quickReactions.forEach { emoji ->
                    Text(
                        emoji,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onReact(emoji) }
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SystemMessage(message: ChatMessage) {
    val (bg, icon) = when (message.messageType) {
        MessageType.ACHIEVEMENT -> Pair(GoldYellow.copy(0.15f), GoldYellow)
        MessageType.CHALLENGE_COMPLETE -> Pair(NeonGreen.copy(0.15f), NeonGreen)
        else -> Pair(ElectricPurple.copy(0.15f), ElectricPurple)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(bg)
                .border(1.dp, icon.copy(0.4f), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(message.content, color = icon, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ReactionChip(emoji: String, count: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCardElevated)
            .border(1.dp, DarkBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 11.sp)
            Text("$count", fontSize = 10.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun AvatarCircle(username: String, size: Dp = 32.dp, fontSize: TextUnit = 14.sp) {
    val colors = listOf(ElectricPurple, NeonBlue, NeonGreen, GoldYellow, CrimsonRed, NeonOrange)
    val color = colors[username.hashCode().and(0x7FFFFFFF) % colors.size]
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Brush.radialGradient(listOf(color, color.copy(0.6f)))),
        contentAlignment = Alignment.Center
    ) {
        Text(username.firstOrNull()?.uppercaseChar()?.toString() ?: "?", fontSize = fontSize, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
private fun PulsingDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(0.3f, 1f, infiniteRepeatable(tween(800), RepeatMode.Reverse), label = "a")
    Box(Modifier.size(8.dp).clip(CircleShape).background(NeonGreen.copy(alpha)))
}

@Composable
private fun TypingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    val offset1 by infiniteTransition.animateFloat(-4f, 4f, infiniteRepeatable(tween(400), RepeatMode.Reverse), label = "d1")
    val offset2 by infiniteTransition.animateFloat(-4f, 4f, infiniteRepeatable(tween(400, delayMillis = 133), RepeatMode.Reverse), label = "d2")
    val offset3 by infiniteTransition.animateFloat(-4f, 4f, infiniteRepeatable(tween(400, delayMillis = 266), RepeatMode.Reverse), label = "d3")

    Row(
        modifier = Modifier
            .padding(start = 8.dp, top = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkCard)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(offset1, offset2, offset3).forEach { off ->
            Box(
                Modifier
                    .size(6.dp)
                    .offset(y = off.dp)
                    .clip(CircleShape)
                    .background(TextMuted)
            )
        }
    }
}

@Composable
private fun GlassInputBar(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(listOf(Color.Transparent, DarkBackground.copy(0.95f)))
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(DarkCard)
                .border(1.dp, DarkBorder, RoundedCornerShape(28.dp))
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message the community...", color = TextMuted, fontSize = 14.sp) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = NeonGreen
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() }),
                maxLines = 3,
                singleLine = false
            )
            AnimatedVisibility(visible = value.isNotBlank(), enter = scaleIn(), exit = scaleOut()) {
                IconButton(
                    onClick = onSend,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(ElectricPurple, NeonBlue)))
                ) {
                    Icon(Icons.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
