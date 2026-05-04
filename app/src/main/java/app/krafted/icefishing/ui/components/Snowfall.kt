package app.krafted.icefishing.ui.components

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.isActive
import kotlin.random.Random

private class Snowflake(
    var x: Float,
    var y: Float,
    var radius: Float,
    var speed: Float,
    var angle: Float,
    var alpha: Float
)

private class SnowState(val size: IntSize) {
    val snowflakes = Array(80) {
        Snowflake(
            x = Random.nextFloat() * size.width,
            y = Random.nextFloat() * size.height,
            radius = Random.nextFloat() * 4f + 2f,
            speed = Random.nextFloat() * 2f + 1f,
            angle = Random.nextFloat() * 2f * Math.PI.toFloat(),
            alpha = Random.nextFloat() * 0.5f + 0.3f
        )
    }

    fun update() {
        for (i in snowflakes.indices) {
            val flake = snowflakes[i]
            flake.x += kotlin.math.sin(flake.angle.toDouble()).toFloat() * 1f
            flake.y += flake.speed
            flake.angle += 0.02f

            if (flake.y > size.height + flake.radius) {
                flake.y = -flake.radius
                flake.x = Random.nextFloat() * size.width
            }
            if (flake.x > size.width + flake.radius) flake.x = -flake.radius
            else if (flake.x < -flake.radius) flake.x = size.width.toFloat() + flake.radius
        }
    }
}

@Composable
fun SnowfallBackground(modifier: Modifier = Modifier) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val snowState = remember(size) {
        if (size != IntSize.Zero) SnowState(size) else null
    }

    // This state triggers recomposition for drawing
    var tick by remember { mutableStateOf(0L) }

    LaunchedEffect(snowState) {
        if (snowState != null) {
            while (isActive) {
                withFrameNanos { frameTimeNanos ->
                    snowState.update()
                    tick = frameTimeNanos
                }
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { size = it }
    ) {
        if (snowState != null) {
            // Read tick to observe state changes
            tick.hashCode()
            
            for (flake in snowState.snowflakes) {
                drawCircle(
                    color = Color.White.copy(alpha = flake.alpha),
                    radius = flake.radius,
                    center = Offset(flake.x, flake.y)
                )
            }
        }
    }
}
