package app.krafted.icefishing.ui.fishguide

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import app.krafted.icefishing.data.model.Species
import app.krafted.icefishing.ui.assets.IceFishAssets
import app.krafted.icefishing.ui.components.SnowfallBackground
import app.krafted.icefishing.ui.theme.IceCyan
import app.krafted.icefishing.ui.theme.IceOnBackground
import app.krafted.icefishing.ui.theme.iceColors
import app.krafted.icefishing.viewmodel.FishDetailUiState
import app.krafted.icefishing.viewmodel.FishDetailViewModel
import kotlin.runCatching
import android.graphics.Color as AndroidColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FishDetailScreen(speciesId: Int, navController: NavController) {
    val app = LocalContext.current.applicationContext as Application
    val viewModel: FishDetailViewModel = viewModel(
        key = "fish_detail_$speciesId",
        factory = FishDetailViewModel.factory(app, speciesId),
    )
    val state by viewModel.state.collectAsState()
    var selectedTab by remember(speciesId) { mutableIntStateOf(0) }

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
                            text = "Fish Guide",
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
            when (val s = state) {
                is FishDetailUiState.Loading -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                }

                is FishDetailUiState.NotFound -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Species not found.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                is FishDetailUiState.Ready -> {
                    val accent = runCatching {
                        Color(AndroidColor.parseColor(s.species.accentColor))
                    }.getOrElse { MaterialTheme.iceColors.cyan }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                    ) {
                        HeroSection(species = s.species, accent = accent)
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = Color.Black.copy(alpha = 0.4f),
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                    color = accent
                                )
                            }
                        ) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = {
                                    Text(
                                        "Habitat",
                                        color = if (selectedTab == 0) accent else MaterialTheme.colorScheme.onBackground
                                    )
                                },
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = {
                                    Text(
                                        "Bait & tackle",
                                        color = if (selectedTab == 1) accent else MaterialTheme.colorScheme.onBackground
                                    )
                                },
                            )
                            Tab(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                text = {
                                    Text(
                                        "Season",
                                        color = if (selectedTab == 2) accent else MaterialTheme.colorScheme.onBackground
                                    )
                                },
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            when (selectedTab) {
                                0 -> HabitatTab(s.species)
                                1 -> BaitTab(s.species)
                                2 -> SeasonTab(s.species, accent)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroSection(species: Species, accent: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(228.dp),
    ) {
        Image(
            painter = painterResource(IceFishAssets.resolve(species.background)),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f),
                        ),
                    ),
                ),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Fish name - always use white for visibility, with accent as glow
            Text(
                text = species.name,
                style = MaterialTheme.typography.headlineMedium.copy(
                    shadow = Shadow(
                        color = accent.copy(alpha = 0.9f),
                        offset = Offset(0f, 0f),
                        blurRadius = 16f
                    )
                ),
                color = Color.White,
            )
            // Hero fact with white text for guaranteed readability
            Text(
                text = species.heroFact,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
            )
        }
    }
}

@Composable
private fun GradientCard(content: @Composable () -> Unit) {
    val cardShape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
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
            .clip(cardShape)
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
    ) {
        content()
    }
}

@Composable
private fun HabitatTab(species: Species) {
    species.habitat.forEach { paragraph ->
        GradientCard {
            Text(
                text = paragraph,
                style = MaterialTheme.typography.bodyLarge,
                color = IceOnBackground,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
private fun BaitTab(species: Species) {
    species.baitAndTackle.forEach { paragraph ->
        GradientCard {
            Text(
                text = paragraph,
                style = MaterialTheme.typography.bodyLarge,
                color = IceOnBackground,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
private fun SeasonTab(species: Species, accent: Color) {
    val season = species.season
    GradientCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Peak months",
                style = MaterialTheme.typography.titleMedium,
                color = IceCyan
            )
            Text(
                text = season.peakMonths.joinToString(", "),
                style = MaterialTheme.typography.bodyLarge,
                color = IceOnBackground
            )
            Text(
                text = "Winter behavior",
                style = MaterialTheme.typography.titleMedium,
                color = IceCyan
            )
            Text(
                text = season.behavior,
                style = MaterialTheme.typography.bodyLarge,
                color = IceOnBackground
            )
            Text(
                text = "Tips",
                style = MaterialTheme.typography.titleMedium,
                color = IceCyan
            )
            season.tips.forEach { tip ->
                Text(
                    text = "· $tip",
                    style = MaterialTheme.typography.bodyLarge,
                    color = IceOnBackground
                )
            }
        }
    }
}
