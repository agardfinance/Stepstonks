package com.stepstonks.app.presentation.screens.community.forum

import androidx.lifecycle.ViewModel
import com.stepstonks.app.domain.model.ForumCategory
import com.stepstonks.app.domain.model.ForumPost
import com.stepstonks.app.domain.model.MOCK_FORUM_POSTS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ForumUiState(
    val posts: List<ForumPost> = MOCK_FORUM_POSTS,
    val selectedCategory: ForumCategory = ForumCategory.ALL,
    val showCreatePost: Boolean = false
)

@HiltViewModel
class ForumViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ForumUiState())
    val uiState: StateFlow<ForumUiState> = _uiState.asStateFlow()

    fun selectCategory(category: ForumCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun toggleLike(postId: String) {
        val posts = _uiState.value.posts.map { post ->
            if (post.id == postId) {
                post.copy(
                    isLiked = !post.isLiked,
                    likes = if (post.isLiked) post.likes - 1 else post.likes + 1
                )
            } else post
        }
        _uiState.value = _uiState.value.copy(posts = posts)
    }

    fun filteredPosts(): List<ForumPost> {
        val cat = _uiState.value.selectedCategory
        return if (cat == ForumCategory.ALL) _uiState.value.posts
        else _uiState.value.posts.filter { it.category == cat }
    }
}
