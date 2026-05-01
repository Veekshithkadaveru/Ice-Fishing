package app.krafted.icefishing.data.db

import androidx.room.*
import app.krafted.icefishing.data.db.entities.QuizScore
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizScoreDao {
    @Query("SELECT * FROM quiz_scores")
    fun getAll(): Flow<List<QuizScore>>

    @Query("SELECT * FROM quiz_scores WHERE categoryId = :categoryId")
    fun getByCategory(categoryId: String): Flow<QuizScore?>

    @Upsert
    suspend fun upsert(score: QuizScore)
}
