package app.krafted.icefishing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.icefishing.data.model.Tip
import app.krafted.icefishing.data.repository.TipRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class TipCategory(
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val tipCount: Int
)

sealed interface TipsUiState {
    data object Loading : TipsUiState
    data class Ready(
        val categories: List<TipCategory>,
        val tipsByCategory: Map<String, List<Tip>>
    ) : TipsUiState
}

class TipsViewModel(application: Application) : AndroidViewModel(application) {
    private val gson = Gson()
    private val repository = TipRepository(application.applicationContext, gson)

    private val _state = MutableStateFlow<TipsUiState>(TipsUiState.Loading)
    val state: StateFlow<TipsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadTips()
        }
    }

    private suspend fun loadTips() {
        val tips = withContext(Dispatchers.IO) {
            repository.getTips()
        }

        val tipsByCategory = tips.groupBy { it.categoryId }

        val categories = tipsByCategory.map { (categoryId, tipList) ->
            val firstTip = tipList.first()
            TipCategory(
                categoryId = categoryId,
                categoryName = firstTip.categoryName,
                categoryIcon = firstTip.categoryIcon,
                tipCount = tipList.size
            )
        }.sortedBy { it.categoryName }

        _state.value = TipsUiState.Ready(categories, tipsByCategory)
    }
}
