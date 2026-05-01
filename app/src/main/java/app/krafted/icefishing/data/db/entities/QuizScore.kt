package app.krafted.icefishing.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_scores")
data class QuizScore(
    @PrimaryKey val categoryId: String,
    val bestScore: Int,
    val lastScore: Int,
    val totalQuestions: Int,
    val completed: Boolean
)
