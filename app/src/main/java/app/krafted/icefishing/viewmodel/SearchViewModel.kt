package app.krafted.icefishing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.icefishing.data.db.AppDatabase
import app.krafted.icefishing.data.model.SearchResult
import app.krafted.icefishing.data.repository.ArticleRepository
import app.krafted.icefishing.data.repository.QuizRepository
import app.krafted.icefishing.data.repository.SearchRepository
import app.krafted.icefishing.data.repository.SpeciesRepository
import app.krafted.icefishing.data.repository.TipRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val gson = Gson()
    private val database = AppDatabase.getInstance(application)
    private val searchRepository = SearchRepository(
        speciesRepository = SpeciesRepository(application, gson),
        articleRepository = ArticleRepository(application, gson, database.bookmarkDao()),
        tipRepository = TipRepository(application, gson),
        quizRepository = QuizRepository(application, gson, database.quizScoreDao())
    )

    fun search(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            _isSearching.value = true
            try {
                _searchResults.value = searchRepository.search(query)
            } finally {
                _isSearching.value = false
            }
        }
    }
}
