package app.krafted.icefishing.data.model

data class QuizQuestion(
    val id: Int,
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)
