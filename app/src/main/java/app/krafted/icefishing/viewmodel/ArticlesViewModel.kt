package app.krafted.icefishing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.icefishing.data.db.AppDatabase
import app.krafted.icefishing.data.model.Article
import app.krafted.icefishing.data.repository.ArticleRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed interface ArticlesUiState {
    data object Loading : ArticlesUiState
    data class Ready(
        val articles: List<Article>,
        val selectedCategory: String?,
        val categories: List<String>,
    ) : ArticlesUiState
}

class ArticlesViewModel(application: Application) : AndroidViewModel(application) {

    private val gson = Gson()
    private val bookmarkDao = AppDatabase.getInstance(application.applicationContext).bookmarkDao()
    private val repository = ArticleRepository(application.applicationContext, gson, bookmarkDao)

    private val _state = MutableStateFlow<ArticlesUiState>(ArticlesUiState.Loading)
    val state: StateFlow<ArticlesUiState> = _state.asStateFlow()

    private var allArticles: List<Article> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            allArticles = repository.getArticles()
            val categories = allArticles.map { it.category }.distinct().sorted()
            _state.value = ArticlesUiState.Ready(allArticles, null, categories)
        }
    }

    fun selectCategory(category: String?) {
        val current = _state.value
        if (current is ArticlesUiState.Ready) {
            val filtered =
                if (category == null) allArticles else allArticles.filter { it.category == category }
            _state.value = current.copy(
                articles = filtered,
                selectedCategory = category,
            )
        }
    }

    fun isBookmarked(articleId: Int): Flow<Boolean> = repository.isBookmarked(articleId)
}
