package app.krafted.icefishing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import app.krafted.icefishing.data.db.AppDatabase
import app.krafted.icefishing.data.db.entities.QuizScore
import app.krafted.icefishing.data.model.QuizQuestion
import app.krafted.icefishing.data.repository.QuizRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class QuizCategory(
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val questionCount: Int
)

sealed interface QuizHomeUiState {
    data object Loading : QuizHomeUiState
    data class Ready(val categories: List<QuizCategory>) : QuizHomeUiState
}

data class QuizSessionState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: Int? = null,
    val answered: Boolean = false,
    val finished: Boolean = false
) {
    val currentQuestion: QuizQuestion? get() = questions.getOrNull(currentIndex)
    val totalQuestions: Int get() = questions.size
    val isCorrect: Boolean get() = selectedAnswer == currentQuestion?.correctIndex
}

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val gson = Gson()
    private val db = AppDatabase.getInstance(application.applicationContext)
    private val repository = QuizRepository(application.applicationContext, gson, db.quizScoreDao())

    private val _homeState = MutableStateFlow<QuizHomeUiState>(QuizHomeUiState.Loading)
    val homeState: StateFlow<QuizHomeUiState> = _homeState.asStateFlow()

    val allScores: StateFlow<List<QuizScore>> = repository.getAllScores()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _sessionState = MutableStateFlow(QuizSessionState())
    val sessionState: StateFlow<QuizSessionState> = _sessionState.asStateFlow()

    init {
        viewModelScope.launch { loadCategories() }
    }

    private suspend fun loadCategories() {
        val questions = withContext(Dispatchers.IO) { repository.getQuestions() }
        val grouped = questions.groupBy { it.categoryId }
        val categories = grouped.map { (categoryId, list) ->
            val first = list.first()
            QuizCategory(
                categoryId = categoryId,
                categoryName = first.categoryName,
                categoryIcon = first.categoryIcon,
                questionCount = list.size
            )
        }.sortedBy { it.categoryName }
        _homeState.value = QuizHomeUiState.Ready(categories)
    }

    fun startQuiz(categoryId: String) {
        viewModelScope.launch {
            val questions = withContext(Dispatchers.IO) {
                repository.getQuestionsByCategory(categoryId)
            }
            _sessionState.value = QuizSessionState(questions = questions)
        }
    }

    fun selectAnswer(optionIndex: Int) {
        val current = _sessionState.value
        if (current.answered) return
        val correct = current.currentQuestion?.correctIndex == optionIndex
        _sessionState.value = current.copy(
            selectedAnswer = optionIndex,
            answered = true,
            score = if (correct) current.score + 1 else current.score
        )
    }

    fun nextQuestion() {
        val current = _sessionState.value
        val nextIndex = current.currentIndex + 1
        if (nextIndex >= current.totalQuestions) {
            _sessionState.value = current.copy(finished = true)
            saveScore(current)
        } else {
            _sessionState.value = current.copy(
                currentIndex = nextIndex,
                selectedAnswer = null,
                answered = false
            )
        }
    }

    private fun saveScore(state: QuizSessionState) {
        val categoryId = state.questions.firstOrNull()?.categoryId ?: return
        viewModelScope.launch {
            val existing = allScores.value.find { it.categoryId == categoryId }
            val bestScore = maxOf(state.score, existing?.bestScore ?: 0)
            repository.saveScore(
                QuizScore(
                    categoryId = categoryId,
                    bestScore = bestScore,
                    lastScore = state.score,
                    totalQuestions = state.totalQuestions,
                    completed = true
                )
            )
        }
    }

    fun getCategoryName(categoryId: String): String {
        return (homeState.value as? QuizHomeUiState.Ready)
            ?.categories?.find { it.categoryId == categoryId }?.categoryName ?: categoryId
    }
}
