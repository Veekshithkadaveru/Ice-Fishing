package app.krafted.icefishing.data.repository

import android.content.Context
import app.krafted.icefishing.data.db.QuizScoreDao
import app.krafted.icefishing.data.db.entities.QuizScore
import app.krafted.icefishing.data.model.QuizQuestion
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class QuizRepository(
    private val context: Context,
    private val gson: Gson,
    private val quizScoreDao: QuizScoreDao
) {
    private var cachedQuestions: List<QuizQuestion>? = null

    suspend fun getQuestions(): List<QuizQuestion> {
        if (cachedQuestions == null) {
            cachedQuestions = loadJsonAsset<List<QuizQuestion>>(context, gson, "quiz.json")
        }
        return cachedQuestions ?: emptyList()
    }

    suspend fun getQuestionsByCategory(categoryId: String): List<QuizQuestion> {
        return getQuestions().filter { it.categoryId == categoryId }
    }
    
    suspend fun getCategories(): List<String> {
        return getQuestions().map { it.categoryId }.distinct()
    }

    fun getScoreByCategory(categoryId: String): Flow<QuizScore?> = quizScoreDao.getByCategory(categoryId)

    fun getAllScores(): Flow<List<QuizScore>> = quizScoreDao.getAll()

    suspend fun saveScore(score: QuizScore) {
        quizScoreDao.upsert(score)
    }
}
