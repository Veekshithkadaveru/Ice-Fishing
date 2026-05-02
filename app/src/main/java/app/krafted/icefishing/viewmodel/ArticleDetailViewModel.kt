package app.krafted.icefishing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.krafted.icefishing.data.db.AppDatabase
import app.krafted.icefishing.data.model.Article
import app.krafted.icefishing.data.repository.ArticleRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface ArticleDetailUiState {
    data object Loading : ArticleDetailUiState
    data class Ready(val article: Article, val isBookmarked: Boolean) : ArticleDetailUiState
    data object NotFound : ArticleDetailUiState
}

class ArticleDetailViewModel(
    application: Application,
    private val articleId: Int,
) : AndroidViewModel(application) {

    private val gson = Gson()
    private val bookmarkDao = AppDatabase.getInstance(application.applicationContext).bookmarkDao()
    private val repository = ArticleRepository(application.applicationContext, gson, bookmarkDao)

    private val _state = MutableStateFlow<ArticleDetailUiState>(ArticleDetailUiState.Loading)
    val state: StateFlow<ArticleDetailUiState> = _state.asStateFlow()

    init {
        when {
            articleId <= 0 -> _state.value = ArticleDetailUiState.NotFound
            else -> viewModelScope.launch {
                val article = repository.getArticleById(articleId)
                val isBookmarked = repository.isBookmarked(articleId).first()
                _state.value = article?.let {
                    ArticleDetailUiState.Ready(it, isBookmarked)
                } ?: ArticleDetailUiState.NotFound
            }
        }
    }

    fun toggleBookmark() {
        val current = _state.value
        if (current is ArticleDetailUiState.Ready) {
            viewModelScope.launch {
                repository.toggleBookmark(current.article.id)
                val newBookmarkState = repository.isBookmarked(current.article.id).first()
                _state.value = current.copy(isBookmarked = newBookmarkState)
            }
        }
    }

    companion object {
        fun factory(
            application: Application,
            articleId: Int,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ArticleDetailViewModel(application, articleId) as T
            }
        }
    }
}
