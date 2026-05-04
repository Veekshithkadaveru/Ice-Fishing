package app.krafted.icefishing.ui.tools

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.krafted.icefishing.ui.assets.IceFishAssets
import app.krafted.icefishing.ui.components.SnowfallBackground
import app.krafted.icefishing.ui.theme.IceDanger
import app.krafted.icefishing.ui.theme.IceSuccess
import app.krafted.icefishing.ui.theme.IceWarning
import app.krafted.icefishing.ui.theme.iceColors
import app.krafted.icefishing.viewmodel.IceCalculatorUiState
import app.krafted.icefishing.viewmodel.IceCalculatorViewModel
import app.krafted.icefishing.viewmodel.IceSafetyResult
import app.krafted.icefishing.viewmodel.IceType
import app.krafted.icefishing.viewmodel.WarningLevel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IceCalculatorScreen(
    navController: NavController,
    viewModel: IceCalculatorViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

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
                .background(MaterialTheme.iceColors.scrim)
        )

        SnowfallBackground(modifier = Modifier.fillMaxSize())

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Ice Safety Calculator",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Black.copy(alpha = 0.7f),
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                        actionIconContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
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
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    HeaderIcon()
                }

                item {
                    FrostedPanel {
                        ThicknessSection(
                            thicknessCm = state.thicknessCm,
                            onThicknessChange = viewModel::setThickness
                        )
                    }
                }

                item {
                    FrostedPanel {
                        IceTypeSection(
                            selectedType = state.iceType,
                            onTypeSelected = viewModel::setIceType
                        )
                    }
                }

                item {
                    ResultPanel(state = state)
                }
            }
        }
    }
}

@Composable
private fun HeaderIcon() {
    Box(
        modifier = Modifier
            .size(78.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.iceColors.cyan.copy(alpha = 0.4f),
                        Color.Transparent
                    ),
                    radius = 140f
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.iceColors.cyan.copy(alpha = 0.9f),
                        MaterialTheme.iceColors.cyan.copy(alpha = 0.1f)
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = IceFishAssets.resolve("icefish_sym_7")),
            contentDescription = "Ice safety",
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun FrostedPanel(content: @Composable () -> Unit) {
    val panelShape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(panelShape)
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
            .clip(panelShape)
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
            .padding(20.dp)
    ) {
        content()
    }
}

@Composable
private fun ThicknessSection(
    thicknessCm: Int,
    onThicknessChange: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ice Thickness",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = thicknessCm.toString(),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.iceColors.cyanLight
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "cm",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }

        Slider(
            value = thicknessCm.toFloat(),
            onValueChange = { onThicknessChange(it.roundToInt()) },
            valueRange = 1f..60f,
            steps = 58,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.onBackground,
                activeTrackColor = MaterialTheme.iceColors.cyan,
                inactiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.22f),
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "1cm",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "60cm",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun IceTypeSection(
    selectedType: IceType,
    onTypeSelected: (IceType) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ice Type",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IceTypeOption(
                iceType = IceType.CLEAR_BLUE,
                selected = selectedType == IceType.CLEAR_BLUE,
                title = "Clear Blue",
                subtitle = "Full strength",
                accent = Color(0xFF1565C0),
                onClick = onTypeSelected,
                modifier = Modifier.weight(1f)
            )
            IceTypeOption(
                iceType = IceType.WHITE_OPAQUE,
                selected = selectedType == IceType.WHITE_OPAQUE,
                title = "White",
                subtitle = "Half strength",
                accent = Color(0xFF78909C),
                onClick = onTypeSelected,
                modifier = Modifier.weight(1f)
            )
            IceTypeOption(
                iceType = IceType.GREY,
                selected = selectedType == IceType.GREY,
                title = "Grey",
                subtitle = "Exit now",
                accent = Color(0xFF9CA3AF),
                onClick = onTypeSelected,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun IceTypeOption(
    iceType: IceType,
    selected: Boolean,
    title: String,
    subtitle: String,
    accent: Color,
    onClick: (IceType) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier
            .height(76.dp)
            .clip(shape)
            .background(
                if (selected) {
                    accent.copy(alpha = 0.24f)
                } else {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.06f)
                }
            )
            .border(
                border = BorderStroke(
                    width = if (selected) 2.dp else 1.dp,
                    color = if (selected) accent else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
                ),
                shape = shape
            )
            .clickable { onClick(iceType) }
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun ResultPanel(state: IceCalculatorUiState) {
    val result = state.result
    val accent = result.warningColor()
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        accent.copy(alpha = 0.25f),
                        accent.copy(alpha = 0.1f)
                    )
                )
            )
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        accent.copy(alpha = 0.8f),
                        accent.copy(alpha = 0.3f)
                    )
                ),
                shape = shape
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.2f))
                    .border(2.dp, accent.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = result.icon(),
                    contentDescription = result.label,
                    tint = accent,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = result.label,
                style = MaterialTheme.typography.headlineSmall,
                color = accent,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.2f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Effective thickness: ${result.effectiveThicknessCm} cm",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }

            if (state.iceType == IceType.WHITE_OPAQUE) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "White ice counts as half strength",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = result.advice,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun IceSafetyResult.warningColor(): Color = when (warningLevel) {
    WarningLevel.DANGER -> IceDanger
    WarningLevel.CAUTION -> IceWarning
    WarningLevel.SAFE -> IceSuccess
    WarningLevel.VERY_SAFE -> IceSuccess
}

private fun IceSafetyResult.icon() = when (warningLevel) {
    WarningLevel.DANGER, WarningLevel.CAUTION -> Icons.Default.Warning
    WarningLevel.SAFE, WarningLevel.VERY_SAFE -> Icons.Default.CheckCircle
}
