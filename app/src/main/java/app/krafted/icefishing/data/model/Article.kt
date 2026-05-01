package app.krafted.icefishing.data.model

data class Article(
    val id: Int,
    val title: String,
    val category: String,
    val readTimeMin: Int,
    val body: String
)
