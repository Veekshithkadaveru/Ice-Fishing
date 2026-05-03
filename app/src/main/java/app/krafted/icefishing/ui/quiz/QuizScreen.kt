package app.krafted.icefishing.ui.quiz

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.krafted.icefishing.navigation.Screen
import app.krafted.icefishing.ui.assets.IceFishAssets
import app.krafted.icefishing.ui.components.SnowfallBackground
import app.krafted.icefishing.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(categoryId: String, navController: NavController) {
    val viewModel: QuizViewModel = viewModel(LocalContext.current as ComponentActivity)
    val session by viewModel.sessionState.collectAsState()

    LaunchedEffect(session.finished) {
        if (session.finished) {
            navController.navigate(Screen.QuizResult.createRoute(categoryId)) {
                popUpTo(Screen.Quiz.createRoute(categoryId)) { inclusive = true }
            }
        }
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
                .background(Color.Black.copy(alpha = 0.55f))
        )

        SnowfallBackground(modifier = Modifier.fillMaxSize())

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = viewModel.getCategoryName(categoryId),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Black.copy(alpha = 0.7f)
                    )
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            val question = session.currentQuestion

            if (question == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                AnimatedContent(
                    targetState = session.currentIndex,
                    transitionSpec = {
                        (slideInHorizontally(tween(400)) { it } + fadeIn(tween(400)))
                            .togetherWith(slideOutHorizontally(tween(400)) { -it } + fadeOut(
                                tween(
                                    400
                                )
                            ))
                    },
                    label = "questionTransition"
                ) { _ ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Question ${session.currentIndex + 1}/${session.totalQuestions}",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Color(0xFFB3E5FC)
                            )
                            Text(
                                text = "Score: ${session.score}",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFFFFB300)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { (session.currentIndex + 1).toFloat() / session.totalQuestions },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = Color(0xFF4DD0E1),
                            trackColor = Color.White.copy(alpha = 0.15f)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        val questionCardShape = RoundedCornerShape(20.dp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(questionCardShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color.White.copy(alpha = 0.2f),
                                            Color.White.copy(alpha = 0.05f)
                                        ),
                                        start = Offset(0f, 0f),
                                        end = Offset(
                                            Float.POSITIVE_INFINITY,
                                            Float.POSITIVE_INFINITY
                                        )
                                    )
                                )
                                .padding(1.5.dp)
                                .clip(questionCardShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF0F172A).copy(alpha = 0.75f),
                                            Color(0xFF1E293B).copy(alpha = 0.55f)
                                        ),
                                        start = Offset(0f, 0f),
                                        end = Offset(0f, Float.POSITIVE_INFINITY)
                                    )
                                )
                                .padding(24.dp)
                        ) {
                            Text(
                                text = question.question,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 26.sp
                                ),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        question.options.forEachIndexed { index, option ->
                            OptionCard(
                                text = option,
                                index = index,
                                selected = session.selectedAnswer == index,
                                isCorrect = index == question.correctIndex,
                                answered = session.answered,
                                onClick = { viewModel.selectAnswer(index) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        if (session.answered) {
                            val nextShape = RoundedCornerShape(16.dp)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp)
                                    .clip(nextShape)
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF4DD0E1),
                                                Color(0xFF0097A7)
                                            )
                                        )
                                    )
                                    .clickable { viewModel.nextQuestion() }
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (session.currentIndex + 1 >= session.totalQuestions) "See Results" else "Next Question",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionCard(
    text: String,
    index: Int,
    selected: Boolean,
    isCorrect: Boolean,
    answered: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            !answered -> Color.White.copy(alpha = 0.2f)
            isCorrect -> Color(0xFF4CAF50)
            selected -> Color(0xFFEF5350)
            else -> Color.White.copy(alpha = 0.1f)
        },
        animationSpec = tween(300),
        label = "borderColor"
    )

    val bgAlpha by animateColorAsState(
        targetValue = when {
            !answered -> Color(0xFF0F172A).copy(alpha = 0.5f)
            isCorrect -> Color(0xFF4CAF50).copy(alpha = 0.15f)
            selected -> Color(0xFFEF5350).copy(alpha = 0.15f)
            else -> Color(0xFF0F172A).copy(alpha = 0.3f)
        },
        animationSpec = tween(300),
        label = "bgColor"
    )

    val optionShape = RoundedCornerShape(16.dp)
    val labels = listOf("A", "B", "C", "D")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(optionShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(borderColor, borderColor.copy(alpha = 0.3f)),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .padding(1.5.dp)
            .clip(optionShape)
            .background(bgAlpha)
            .clickable(enabled = !answered, onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when {
                            answered && isCorrect -> Color(0xFF4CAF50).copy(alpha = 0.3f)
                            answered && selected -> Color(0xFFEF5350).copy(alpha = 0.3f)
                            else -> Color.White.copy(alpha = 0.1f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (answered && (isCorrect || selected)) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFEF5350),
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = labels.getOrElse(index) { "" },
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}
