package app.krafted.icefishing.ui.tools

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.krafted.icefishing.ui.assets.IceFishAssets
import app.krafted.icefishing.ui.components.SnowfallBackground
import app.krafted.icefishing.viewmodel.ChecklistGroup
import app.krafted.icefishing.viewmodel.ChecklistRow
import app.krafted.icefishing.viewmodel.ChecklistViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TackleChecklistScreen(
    navController: NavController,
    viewModel: ChecklistViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

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
                            text = "Tackle Checklist",
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
                    actions = {
                        IconButton(onClick = { showResetDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset checklist",
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
            if (state.groups.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 8.dp,
                        end = 16.dp,
                        bottom = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SummaryPanel(
                            checked = state.totalChecked,
                            total = state.totalItems
                        )
                    }

                    var rowIndex = 0
                    state.groups.forEach { group ->
                        item(key = "header_${group.groupId}") {
                            GroupHeaderCard(group = group)
                        }
                        group.items.forEach { row ->
                            val animationIndex = rowIndex
                            item(key = row.id) {
                                StaggeredItem(index = animationIndex) {
                                    ChecklistItemCard(
                                        row = row,
                                        onToggle = { viewModel.setChecked(row.id, it) }
                                    )
                                }
                            }
                            rowIndex++
                        }
                    }
                }
            }
        }

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Reset checklist?") },
                text = { Text("This will uncheck all 21 items.") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.resetAll()
                        showResetDialog = false
                    }) {
                        Text("Reset", color = Color(0xFFFF8A80))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel")
                    }
                },
                containerColor = Color(0xFF0F172A),
                titleContentColor = Color.White,
                textContentColor = Color(0xFFB3E5FC)
            )
        }
    }
}

@Composable
private fun StaggeredItem(index: Int, content: @Composable () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 40L)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(400)) + slideInVertically(
            animationSpec = tween(400),
            initialOffsetY = { it / 2 }
        )
    ) {
        content()
    }
}

@Composable
private fun SummaryPanel(checked: Int, total: Int) {
    val panelShape = RoundedCornerShape(24.dp)
    val progress = if (total == 0) 0f else checked.toFloat() / total.toFloat()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(panelShape)
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
            .clip(panelShape)
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
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pack Status",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.4.sp
                    ),
                    color = Color.White
                )
                Text(
                    text = "$checked of $total packed",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = Color(0xFF7DD8FF)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF4DD0E1),
                trackColor = Color.White.copy(alpha = 0.15f)
            )
        }
    }
}

@Composable
private fun GroupHeaderCard(group: ChecklistGroup) {
    val cardShape = RoundedCornerShape(20.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.25f),
                        Color.White.copy(alpha = 0.04f)
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
                        Color(0xFF0F172A).copy(alpha = 0.6f),
                        Color(0xFF1E293B).copy(alpha = 0.4f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = group.groupName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.4.sp
                ),
                color = Color.White
            )
            Text(
                text = "${group.checkedCount}/${group.totalCount}",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF4DD0E1)
            )
        }
    }
}

@Composable
private fun ChecklistItemCard(row: ChecklistRow, onToggle: (Boolean) -> Unit) {
    val cardShape = RoundedCornerShape(20.dp)

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
            .clickable { onToggle(!row.checked) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (row.checked) {
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF4DD0E1).copy(alpha = 0.55f),
                                    Color(0xFF4DD0E1).copy(alpha = 0.15f)
                                ),
                                radius = 80f
                            )
                        } else {
                            Brush.radialGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent
                                )
                            )
                        }
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF4DD0E1).copy(alpha = 0.9f),
                                Color(0xFF4DD0E1).copy(alpha = 0.1f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (row.checked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = row.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (row.checked) TextDecoration.LineThrough else TextDecoration.None
                ),
                color = if (row.checked) Color.White.copy(alpha = 0.55f) else Color.White,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
