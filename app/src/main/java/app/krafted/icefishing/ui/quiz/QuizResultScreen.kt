package app.krafted.icefishing.ui.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.krafted.icefishing.navigation.Screen
import app.krafted.icefishing.ui.assets.IceFishAssets
import app.krafted.icefishing.ui.components.SnowfallBackground
import app.krafted.icefishing.ui.theme.IceCyan
import app.krafted.icefishing.ui.theme.iceColors
import app.krafted.icefishing.viewmodel.QuizViewModel
import kotlinx.coroutines.delay

@Composable
fun QuizResultScreen(categoryId: String, navController: NavController) {
    val quizHomeEntry = remember(navController) { navController.getBackStackEntry(Screen.QuizHome.route) }
    val viewModel: QuizViewModel = viewModel(quizHomeEntry)
    val session by viewModel.sessionState.collectAsState()

    val score = session.score
    val total = session.totalQuestions
    val percentage = if (total > 0) score.toFloat() / total else 0f
    val stars = when {
        percentage >= 0.9f -> 3
        percentage >= 0.6f -> 2
        percentage >= 0.3f -> 1
        else -> 0
    }

    var showContent by remember { mutableStateOf(false) }
    var showStars by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
        delay(500)
        showStars = true
        delay(400)
        showButtons = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = IceFishAssets.resolve("icefish_back_5")),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        )

        SnowfallBackground(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600)) + scaleIn(tween(600), initialScale = 0.8f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when {
                            percentage >= 0.9f -> "Outstanding!"
                            percentage >= 0.7f -> "Great Job!"
                            percentage >= 0.5f -> "Not Bad!"
                            else -> "Keep Trying!"
                        },
                        style = MaterialTheme.typography.headlineLarge.copy(
                            shadow = Shadow(
                                color = MaterialTheme.iceColors.cyan.copy(alpha = 0.6f),
                                offset = Offset(0f, 4f),
                                blurRadius = 12f
                            )
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = viewModel.getCategoryName(categoryId),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(600))
            ) {
                val scoreCardShape = RoundedCornerShape(24.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(scoreCardShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.iceColors.borderStart,
                                    MaterialTheme.iceColors.borderEnd
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                            )
                        )
                        .padding(1.5.dp)
                        .clip(scoreCardShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.iceColors.cardBgStart.copy(alpha = 0.75f),
                                    MaterialTheme.iceColors.cardBgEnd.copy(alpha = 0.55f)
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(0f, Float.POSITIVE_INFINITY)
                            )
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val infiniteTransition = rememberInfiniteTransition(label = "scorePulse")
                        val scoreScale by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.05f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1200, easing = EaseInOutSine),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "scoreScale"
                        )

                        val scoreGradient = Brush.linearGradient(
                            colors = listOf(Color.White, IceCyan)
                        )

                        Text(
                            text = "$score / $total",
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontSize = (48 * scoreScale).sp,
                                brush = scoreGradient
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "${(percentage * 100).toInt()}% Correct",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        AnimatedVisibility(
                            visible = showStars,
                            enter = fadeIn(tween(400)) + scaleIn(tween(400))
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                repeat(3) { index ->
                                    Icon(
                                        imageVector = if (index < stars) Icons.Filled.Star else Icons.Outlined.Star,
                                        contentDescription = null,
                                        tint = if (index < stars) MaterialTheme.iceColors.gold else MaterialTheme.colorScheme.onBackground.copy(
                                            alpha = 0.3f
                                        ),
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(
                visible = showButtons,
                enter = fadeIn(tween(500))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val replayShape = RoundedCornerShape(16.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(replayShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.iceColors.cyan,
                                        MaterialTheme.iceColors.cyanDark
                                    )
                                )
                            )
                            .clickable {
                                viewModel.startQuiz(categoryId)
                                navController.navigate(Screen.Quiz.createRoute(categoryId)) {
                                    popUpTo(Screen.QuizResult.createRoute(categoryId)) {
                                        inclusive = true
                                    }
                                }
                            }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Play Again",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    val backShape = RoundedCornerShape(16.dp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(backShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.iceColors.borderStart,
                                        MaterialTheme.iceColors.borderEnd
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                                )
                            )
                            .padding(1.5.dp)
                            .clip(backShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.iceColors.cardBgStart,
                                        MaterialTheme.iceColors.cardBgEnd
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(0f, Float.POSITIVE_INFINITY)
                                )
                            )
                            .clickable {
                                navController.popBackStack(Screen.QuizHome.route, inclusive = false)
                            }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Back to Categories",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}
