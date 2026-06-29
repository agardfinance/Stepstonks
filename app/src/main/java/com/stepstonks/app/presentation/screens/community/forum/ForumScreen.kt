package com.stepstonks.app.presentation.screens.community.forum

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stepstonks.app.domain.model.ForumCategory
import com.stepstonks.app.domain.model.ForumPost
import com.stepstonks.app.presentation.theme.*
import java.util.concurrent.TimeUnit

@Composable
fun ForumScreen(viewModel: ForumViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val filteredPosts = viewModel.filteredPosts()
    val categories = listOf(ForumCategory.ALL, ForumCategory.GENERAL, ForumCategory.TIPS, ForumCategory.TRADING, ForumCategory.ACHIEVEMENTS, ForumCategory.GUIDES)

    Box(Modifier.fillMaxSize().background(DarkBackground)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            item {
                // Category filter
                ScrollableTabRow(
                    selectedTabIndex = categories.indexOf(state.selectedCategory),
                    containerColor = DarkSurface,
                    contentColor = NeonGreen,
                    edgePadding = 8.dp,
                    indicator = {},
                    divider = {}
                ) {
                    categories.forEach { cat ->
                        val isSelected = state.selectedCategory == cat
                        Tab(
                            selected = isSelected,
                            onClick = { viewModel.selectCategory(cat) },
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) NeonGreen.copy(0.2f) else Color.Transparent)
                                    .border(1.dp, if (isSelected) NeonGreen.copy(0.6f) else DarkBorder, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    "${cat.emoji} ${cat.label}",
                                    fontSize = 12.sp,
                                    color = if (isSelected) NeonGreen else TextMuted,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            val pinned = filteredPosts.filter { it.isPinned }
            val regular = filteredPosts.filter { !it.isPinned }

            if (pinned.isNotEmpty()) {
                items(pinned) { post ->
                    PostCard(post = post, onLike = { viewModel.toggleLike(post.id) }, isPinned = true)
                }
            }
            items(regular, key = { it.id }) { post ->
                PostCard(post = post, onLike = { viewModel.toggleLike(post.id) })
            }
        }

        // FAB
        ExtendedFloatingActionButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = Color.Transparent,
            contentColor = Color.White,
            elevation = FloatingActionButtonDefaults.elevation(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .background(Brush.linearGradient(listOf(ElectricPurple, NeonBlue)))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Create, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Text("New Post", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun PostCard(post: ForumPost, onLike: () -> Unit, isPinned: Boolean = false) {
    val categoryColor = when (post.category) {
        ForumCategory.TIPS -> NeonGreen
        ForumCategory.TRADING -> GoldYellow
        ForumCategory.ACHIEVEMENTS -> ElectricPurple
        ForumCategory.GUIDES -> NeonBlue
        else -> TextSecondary
    }

    var likeScale by remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = likeScale,
        animationSpec = spring(Spring.DampingRatioMediumBouncy),
        finishedListener = { likeScale = 1f },
        label = "like"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            if (isPinned) Brush.linearGradient(listOf(GoldYellow.copy(0.5f), GoldYellow.copy(0.2f)))
            else Brush.linearGradient(listOf(categoryColor.copy(0.2f), DarkBorder))
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            // Pin + category row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (isPinned) {
                        Icon(Icons.Filled.PushPin, contentDescription = null, tint = GoldYellow, modifier = Modifier.size(14.dp))
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(categoryColor.copy(0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text("${post.category.emoji} ${post.category.label}", fontSize = 10.sp, color = categoryColor, fontWeight = FontWeight.SemiBold)
                    }
                }
                Text(timeAgo(post.timestamp), fontSize = 11.sp, color = TextMuted)
            }

            Spacer(Modifier.height(8.dp))

            // Title
            Text(
                post.title,
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                post.content,
                fontSize = 13.sp,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            // Tags
            if (post.tags.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    post.tags.take(3).forEach { tag ->
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkCardElevated)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("#$tag", fontSize = 10.sp, color = TextMuted)
                        }
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            HorizontalDivider(color = DarkBorder, thickness = 0.5.dp)
            Spacer(Modifier.height(10.dp))

            // Author + stats row
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    AuthorAvatar(post.authorName, 24.dp)
                    Text(post.authorName, fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.SemiBold)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    // Comments
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.ChatBubbleOutline, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
                        Text("${post.commentCount}", fontSize = 12.sp, color = TextMuted)
                    }
                    // Like button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                likeScale = 1.3f
                                onLike()
                            }
                            .background(if (post.isLiked) CrimsonRed.copy(0.15f) else Color.Transparent)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (post.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = if (post.isLiked) CrimsonRed else TextMuted,
                            modifier = Modifier.size(14.dp).scale(animatedScale)
                        )
                        Text("${post.likes}", fontSize = 12.sp, color = if (post.isLiked) CrimsonRed else TextMuted)
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthorAvatar(username: String, size: Dp) {
    val colors = listOf(ElectricPurple, NeonBlue, NeonGreen, GoldYellow, CrimsonRed)
    val color = colors[username.hashCode().and(0x7FFFFFFF) % colors.size]
    Box(
        Modifier.size(size).clip(CircleShape).background(color.copy(0.3f)).border(1.dp, color.copy(0.6f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(username.firstOrNull()?.uppercaseChar()?.toString() ?: "?", fontSize = (size.value * 0.4f).sp, fontWeight = FontWeight.Bold, color = color)
    }
}

private fun timeAgo(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "just now"
        diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}m ago"
        diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}h ago"
        else -> "${TimeUnit.MILLISECONDS.toDays(diff)}d ago"
    }
}
