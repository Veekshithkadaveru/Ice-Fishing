package app.krafted.icefishing.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.krafted.icefishing.R
import app.krafted.icefishing.ui.components.SnowfallBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class FrostParticle(
    val angle: Float,
    val distance: Float,
    val size: Float,
    val alpha: Float,
    val branchAngle: Float,
    val depth: Int
)

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    val iconScale = remember { Animatable(0f) }
    val iconAlpha = remember { Animatable(0f) }
    val frostProgress = remember { Animatable(0f) }
    val titleAlpha = remember { Animatable(0f) }
    val subtitleAlpha = remember { Animatable(0f) }
    val glowAlpha = remember { Animatable(0f) }
    val ringScale = remember { Animatable(0f) }
    var navigated by remember { mutableStateOf(false) }

    val frostParticles = remember {
        val particles = mutableListOf<FrostParticle>()
        for (branch in 0 until 6) {
            val baseAngle = branch * 60f
            for (d in 0 until 12) {
                val dist = 80f + d * 15f
                particles.add(
                    FrostParticle(
                        angle = baseAngle + Random.nextFloat() * 4f - 2f,
                        distance = dist + Random.nextFloat() * 10f,
                        size = Random.nextFloat() * 3f + 1.5f,
                        alpha = Random.nextFloat() * 0.4f + 0.3f,
                        branchAngle = baseAngle,
                        depth = d
                    )
                )
                if (d > 2 && d < 9) {
                    for (sub in listOf(-30f, 30f)) {
                        particles.add(
                            FrostParticle(
                                angle = baseAngle + sub * (d / 12f),
                                distance = dist + Random.nextFloat() * 8f,
                                size = Random.nextFloat() * 2f + 1f,
                                alpha = Random.nextFloat() * 0.3f + 0.2f,
                                branchAngle = baseAngle + sub,
                                depth = d
                            )
                        )
                    }
                }
            }
        }
        particles
    }

    val infiniteTransition = rememberInfiniteTransition(label = "splashShimmer")
    val shimmerAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    LaunchedEffect(Unit) {
        launch {
            iconAlpha.animateTo(1f, tween(600, easing = EaseOutQuart))
        }
        launch {
            iconScale.animateTo(1f, tween(800, easing = EaseOutBack))
        }
        delay(300)
        launch {
            glowAlpha.animateTo(0.8f, tween(1000, easing = EaseInOutCubic))
        }
        launch {
            ringScale.animateTo(1f, tween(1200, easing = EaseOutQuart))
        }
        delay(200)
        launch {
            frostProgress.animateTo(1f, tween(1500, easing = EaseOutQuart))
        }
        delay(400)
        launch {
            titleAlpha.animateTo(1f, tween(700, easing = EaseOutQuart))
        }
        delay(300)
        launch {
            subtitleAlpha.animateTo(1f, tween(600, easing = EaseOutQuart))
        }
        delay(1200)
        if (!navigated) {
            navigated = true
            onNavigateToHome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF030C17),
                        Color(0xFF0A1929),
                        Color(0xFF0F2A40),
                        Color(0xFF061220)
                    )
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!navigated) {
                    navigated = true
                    onNavigateToHome()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        SnowfallBackground(modifier = Modifier.fillMaxSize())

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .alpha(glowAlpha.value)
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2 - 40.dp.toPx()

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF4DD0E1).copy(alpha = 0.15f),
                        Color(0xFF1A8BA0).copy(alpha = 0.05f),
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY),
                    radius = 300f
                ),
                radius = 300f,
                center = Offset(centerX, centerY)
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .alpha(frostProgress.value)
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2 - 40.dp.toPx()

            drawFrostCrystals(
                particles = frostParticles,
                centerX = centerX,
                centerY = centerY,
                progress = frostProgress.value,
                shimmerAngle = shimmerAngle
            )
        }

        Canvas(
            modifier = Modifier
                .size(180.dp)
                .offset(y = (-40).dp)
                .scale(ringScale.value)
                .alpha(ringScale.value * 0.6f)
        ) {
            val cx = size.width / 2
            val cy = size.height / 2
            val radius = size.width / 2

            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFF4DD0E1).copy(alpha = 0.6f),
                        Color(0xFF80DEEA).copy(alpha = 0.1f),
                        Color(0xFFB2EBF2).copy(alpha = 0.5f),
                        Color(0xFF4DD0E1).copy(alpha = 0.1f),
                        Color(0xFF4DD0E1).copy(alpha = 0.6f)
                    ),
                    center = Offset(cx, cy)
                ),
                radius = radius,
                center = Offset(cx, cy),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(iconScale.value)
                    .alpha(iconAlpha.value)
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .alpha(glowAlpha.value * 0.4f)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF4DD0E1).copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            ),
                            shape = CircleShape
                        )
                )
                Image(
                    painter = painterResource(id = R.drawable.splash_icon),
                    contentDescription = "Ice Fishing Pro",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            val titleGradient = remember {
                Brush.linearGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFB3E5FC),
                        Color(0xFF4DD0E1),
                        Color(0xFFB3E5FC),
                        Color.White
                    )
                )
            }

            Text(
                text = "ICE FISHING PRO",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 6.sp,
                    shadow = Shadow(
                        color = Color(0xFF4DD0E1).copy(alpha = 0.8f),
                        offset = Offset(0f, 4f),
                        blurRadius = 20f
                    ),
                    brush = titleGradient
                ),
                modifier = Modifier.alpha(titleAlpha.value)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your Complete Winter Companion",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp,
                    shadow = Shadow(
                        color = Color(0xFF0A1929),
                        offset = Offset(0f, 2f),
                        blurRadius = 8f
                    )
                ),
                color = Color(0xFFB3E5FC).copy(alpha = 0.9f),
                modifier = Modifier.alpha(subtitleAlpha.value)
            )
        }

        Text(
            text = "Tap to continue",
            style = MaterialTheme.typography.bodySmall.copy(
                letterSpacing = 1.sp
            ),
            color = Color.White.copy(alpha = subtitleAlpha.value * 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        )
    }
}

private fun DrawScope.drawFrostCrystals(
    particles: List<FrostParticle>,
    centerX: Float,
    centerY: Float,
    progress: Float,
    shimmerAngle: Float
) {
    for (particle in particles) {
        val particleProgress = (progress * 12f - particle.depth).coerceIn(0f, 1f)
        if (particleProgress <= 0f) continue

        val rad = Math.toRadians(particle.angle.toDouble())
        val px = centerX + cos(rad).toFloat() * particle.distance
        val py = centerY + sin(rad).toFloat() * particle.distance

        val shimmerOffset =
            sin(Math.toRadians((shimmerAngle + particle.angle * 3).toDouble())).toFloat()
        val dynamicAlpha = particle.alpha * particleProgress * (0.7f + shimmerOffset * 0.3f)

        drawCircle(
            color = Color(0xFFB2EBF2).copy(alpha = dynamicAlpha * 0.3f),
            radius = particle.size * 3f,
            center = Offset(px, py)
        )

        drawCircle(
            color = Color.White.copy(alpha = dynamicAlpha),
            radius = particle.size * particleProgress,
            center = Offset(px, py)
        )
    }

    for (branch in 0 until 6) {
        val baseAngle = branch * 60f
        val rad = Math.toRadians(baseAngle.toDouble())
        val lineProgress = progress.coerceIn(0f, 1f)
        val lineLen = 250f * lineProgress

        val endX = centerX + cos(rad).toFloat() * lineLen
        val endY = centerY + sin(rad).toFloat() * lineLen

        drawLine(
            color = Color(0xFFE0F7FA).copy(alpha = 0.15f * progress),
            start = Offset(centerX, centerY),
            end = Offset(endX, endY),
            strokeWidth = 1.5f
        )
    }
}
