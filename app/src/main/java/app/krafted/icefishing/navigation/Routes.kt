package app.krafted.icefishing.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object FishGuideHome : Screen("fish_guide_home")
    data class FishDetail(val speciesId: Int) : Screen("fish_detail/{speciesId}") {
        companion object { const val ROUTE = "fish_detail/{speciesId}" }
        fun createRoute(id: Int) = "fish_detail/$id"
    }
    object ArticlesHome : Screen("articles_home")
    data class ArticleDetail(val articleId: Int) : Screen("article_detail/{articleId}") {
        companion object { const val ROUTE = "article_detail/{articleId}" }
        fun createRoute(id: Int) = "article_detail/$id"
    }
    object TipsHome : Screen("tips_home")
    data class TipsCategory(val categoryId: String) : Screen("tips_category/{categoryId}") {
        companion object { const val ROUTE = "tips_category/{categoryId}" }
        fun createRoute(id: String) = "tips_category/$id"
    }
    object QuizHome : Screen("quiz_home")
    data class Quiz(val categoryId: String) : Screen("quiz/{categoryId}") {
        companion object { const val ROUTE = "quiz/{categoryId}" }
        fun createRoute(id: String) = "quiz/$id"
    }
    data class QuizResult(val categoryId: String) : Screen("quiz_result/{categoryId}") {
        companion object { const val ROUTE = "quiz_result/{categoryId}" }
        fun createRoute(id: String) = "quiz_result/$id"
    }
    object IceCalculator : Screen("ice_calculator")
    object CatchLogger : Screen("catch_logger")
    object TackleChecklist : Screen("tackle_checklist")
    object SearchResults : Screen("search_results?query={query}") {
        const val ROUTE = "search_results?query={query}"
        fun createRoute(query: String = "") = "search_results?query=$query"
    }
}
