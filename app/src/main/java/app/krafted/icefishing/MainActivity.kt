package app.krafted.icefishing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.krafted.icefishing.navigation.Screen
import app.krafted.icefishing.ui.HomeScreen
import app.krafted.icefishing.ui.SplashScreen
import app.krafted.icefishing.ui.articles.ArticleDetailScreen
import app.krafted.icefishing.ui.articles.ArticlesHomeScreen
import app.krafted.icefishing.ui.fishguide.FishDetailScreen
import app.krafted.icefishing.ui.fishguide.FishGuideHomeScreen
import app.krafted.icefishing.ui.quiz.QuizHomeScreen
import app.krafted.icefishing.ui.quiz.QuizResultScreen
import app.krafted.icefishing.ui.quiz.QuizScreen
import app.krafted.icefishing.ui.search.SearchResultsScreen
import app.krafted.icefishing.ui.theme.IceFishingTheme
import app.krafted.icefishing.ui.tips.TipsCategoryScreen
import app.krafted.icefishing.ui.tips.TipsHomeScreen
import app.krafted.icefishing.ui.tools.CatchLoggerScreen
import app.krafted.icefishing.ui.tools.IceCalculatorScreen
import app.krafted.icefishing.ui.tools.TackleChecklistScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IceFishingTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    IceFishingNavHost()
                }
            }
        }
    }
}

@Composable
fun IceFishingNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.FishGuideHome.route) {
            FishGuideHomeScreen(navController = navController)
        }
        composable(
            route = Screen.FishDetail.ROUTE,
            arguments = listOf(navArgument("speciesId") { type = NavType.IntType })
        ) { backStackEntry ->
            FishDetailScreen(
                speciesId = backStackEntry.arguments?.getInt("speciesId") ?: 0,
                navController = navController
            )
        }
        composable(Screen.ArticlesHome.route) {
            ArticlesHomeScreen(navController = navController)
        }
        composable(
            route = Screen.ArticleDetail.ROUTE,
            arguments = listOf(navArgument("articleId") { type = NavType.IntType })
        ) { backStackEntry ->
            ArticleDetailScreen(
                articleId = backStackEntry.arguments?.getInt("articleId") ?: 0,
                navController = navController
            )
        }
        composable(Screen.TipsHome.route) {
            TipsHomeScreen(navController = navController)
        }
        composable(
            route = Screen.TipsCategory.ROUTE,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            TipsCategoryScreen(
                categoryId = backStackEntry.arguments?.getString("categoryId") ?: "",
                navController = navController
            )
        }
        composable(Screen.QuizHome.route) {
            QuizHomeScreen(navController = navController)
        }
        composable(
            route = Screen.Quiz.ROUTE,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            QuizScreen(
                categoryId = backStackEntry.arguments?.getString("categoryId") ?: "",
                navController = navController
            )
        }
        composable(
            route = Screen.QuizResult.ROUTE,
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            QuizResultScreen(
                categoryId = backStackEntry.arguments?.getString("categoryId") ?: "",
                navController = navController
            )
        }
        composable(Screen.IceCalculator.route) {
            IceCalculatorScreen(navController = navController)
        }
        composable(Screen.CatchLogger.route) {
            CatchLoggerScreen(navController = navController)
        }
        composable(Screen.TackleChecklist.route) {
            TackleChecklistScreen(navController = navController)
        }
        composable(
            route = Screen.SearchResults.ROUTE,
            arguments = listOf(navArgument("query") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            SearchResultsScreen(
                query = backStackEntry.arguments?.getString("query") ?: "",
                navController = navController
            )
        }
    }
}
