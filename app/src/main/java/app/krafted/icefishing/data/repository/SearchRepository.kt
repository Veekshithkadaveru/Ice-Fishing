package app.krafted.icefishing.data.repository

import app.krafted.icefishing.data.model.ContentType
import app.krafted.icefishing.data.model.SearchResult

class SearchRepository(
    private val speciesRepository: SpeciesRepository,
    private val articleRepository: ArticleRepository,
    private val tipRepository: TipRepository,
    private val quizRepository: QuizRepository
) {
    suspend fun search(query: String): List<SearchResult> {
        val normalizedQuery = query.trim().lowercase()
        if (normalizedQuery.isEmpty()) return emptyList()

        val results = mutableListOf<SearchResult>()

        // 1. Search Species
        val speciesList = speciesRepository.getSpecies()
        speciesList.forEach { species ->
            if (species.name.lowercase().contains(normalizedQuery) ||
                species.heroFact.lowercase().contains(normalizedQuery)
            ) {
                results.add(
                    SearchResult(
                        type = ContentType.FISH,
                        title = species.name,
                        preview = species.heroFact,
                        id = species.id.toString()
                    )
                )
            }
        }

        // 2. Search Articles
        val articles = articleRepository.getArticles()
        articles.forEach { article ->
            if (article.title.lowercase().contains(normalizedQuery) ||
                article.body.lowercase().contains(normalizedQuery)
            ) {
                results.add(
                    SearchResult(
                        type = ContentType.ARTICLE,
                        title = article.title,
                        preview = article.body.take(100).replace("\n", " ").trim() + "...",
                        id = article.id.toString()
                    )
                )
            }
        }

        // 3. Search Tips
        val tips = tipRepository.getTips()
        tips.forEach { tip ->
            if (tip.title.lowercase().contains(normalizedQuery) ||
                tip.body.lowercase().contains(normalizedQuery)
            ) {
                results.add(
                    SearchResult(
                        type = ContentType.TIP,
                        title = tip.title,
                        preview = tip.body.take(100).replace("\n", " ").trim() + "...",
                        id = tip.categoryId // navigate to TipsCategoryScreen requires categoryId
                    )
                )
            }
        }

        // 4. Search Quiz
        val questions = quizRepository.getQuestions()
        questions.forEach { quiz ->
            if (quiz.question.lowercase().contains(normalizedQuery) ||
                quiz.options.any { it.lowercase().contains(normalizedQuery) }
            ) {
                results.add(
                    SearchResult(
                        type = ContentType.QUIZ,
                        title = quiz.question,
                        preview = quiz.categoryName,
                        id = quiz.categoryId
                    )
                )
            }
        }

        return results.sortedBy { it.type }
    }
}
