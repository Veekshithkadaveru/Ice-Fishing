package app.krafted.icefishing.ui.quiz

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import app.krafted.icefishing.viewmodel.QuizCategory
import app.krafted.icefishing.viewmodel.QuizHomeUiState
import app.krafted.icefishing.viewmodel.QuizViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizHomeScreen(navController: NavController) {
    val viewModel: QuizViewModel = viewModel(LocalContext.current as ComponentActivity)
    val state by viewModel.homeState.collectAsState()
    val scores by viewModel.allScores.collectAsState()

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
                .background(Color.Black.copy(alpha = 0.5f))
        )

        SnowfallBackground(modifier = Modifier.fillMaxSize())

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Quiz Challenge",
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
            when (val s = state) {
                is QuizHomeUiState.Loading -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }

                is QuizHomeUiState.Ready -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(
                        s.categories,
                        key = { _, cat -> cat.categoryId }) { index, category ->
                        var isVisible by remember { mutableStateOf(false) }

                        LaunchedEffect(key1 = category.categoryId) {
                            delay(index * 100L)
                            isVisible = true
                        }

                        val score = scores.find { it.categoryId == category.categoryId }

                        AnimatedVisibility(
                            visible = isVisible,
                            enter = fadeIn(tween(500)) + slideInVertically(
                                animationSpec = tween(500),
                                initialOffsetY = { it / 2 }
                            )
                        ) {
                            QuizCategoryCard(
                                category = category,
                                bestScore = score?.bestScore,
                                totalQuestions = score?.totalQuestions ?: category.questionCount,
                                completed = score?.completed == true,
                                onClick = {
                                    viewModel.startQuiz(category.categoryId)
                                    navController.navigate(Screen.Quiz.createRoute(category.categoryId))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizCategoryCard(
    category: QuizCategory,
    bestScore: Int?,
    totalQuestions: Int,
    completed: Boolean,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.05f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
            .padding(1.5.dp)
            .clip(cardShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0F172A).copy(alpha = 0.65f),
                        Color(0xFF1E293B).copy(alpha = 0.45f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF4DD0E1).copy(alpha = 0.4f),
                                Color.Transparent
                            ),
                            radius = 120f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = IceFishAssets.resolve(category.categoryIcon)),
                    contentDescription = category.categoryName,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.categoryName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${category.questionCount} questions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFB3E5FC).copy(alpha = 0.8f)
                )
                if (completed && bestScore != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Best: $bestScore/$totalQuestions",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color(0xFFFFB300)
                        )
                    }
                }
            }
        }
    }
}
