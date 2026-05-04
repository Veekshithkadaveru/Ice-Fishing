package app.krafted.icefishing.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Ice Fishing Pro Color Palette
// Primary palette - Deep arctic blues
val IcePrimary = Color(0xFF1A3A5C)
val IcePrimaryVariant = Color(0xFF0D2238)
val IceOnPrimary = Color(0xFFE8F4FD)

// Secondary palette - Cyan accents
val IceSecondary = Color(0xFF2E6DA4)
val IceOnSecondary = Color(0xFFFFFFFF)

// Background colors - Deep navy/arctic dark
val IceBackground = Color(0xFF0A1929)
val IceOnBackground = Color(0xFFDCEEF8)

// Surface colors - Card backgrounds
val IceSurface = Color(0xFF0F2A40)
val IceSurfaceVariant = Color(0xFF1C3A52)
val IceOnSurface = Color(0xFFCAE4F5)
val IceOnSurfaceVariant = Color(0xFF90BACD)

// Accent colors - Frost cyan and ice blue
val IceAccent = Color(0xFFFFB300)
val IceOnAccent = Color(0xFF1A1200)
val IceCyan = Color(0xFF4DD0E1)
val IceCyanLight = Color(0xFFB3E5FC)
val IceCyanDark = Color(0xFF0097A7)
val IceFrost = Color(0xFFE8F4FD)

// Utility colors
val IceSteel = Color(0xFF607D8B)
val IceDeepNavy = Color(0xFF061220)
val IceMidnight = Color(0xFF030C17)

// Semantic colors
val IceDanger = Color(0xFFCF3030)
val IceOnDanger = Color(0xFFFFFFFF)
val IceSuccess = Color(0xFF69F0AE)
val IceWarning = Color(0xFFFFC107)

// Light theme colors
val IcePrimaryLight = Color(0xFF1E5FA8)
val IceOnPrimaryLight = Color(0xFFFFFFFF)
val IceBackgroundLight = Color(0xFFF0F7FF)
val IceOnBackgroundLight = Color(0xFF0A1929)
val IceSurfaceLight = Color(0xFFFFFFFF)
val IceSurfaceVariantLight = Color(0xFFDCEEF8)
val IceOnSurfaceLight = Color(0xFF0D2238)
val IceOnSurfaceVariantLight = Color(0xFF2E6DA4)

// Dark color scheme (default for ice theme)
val IceDarkColorScheme = darkColorScheme(
    primary = IceSecondary,
    onPrimary = IceOnPrimary,
    primaryContainer = IcePrimary,
    onPrimaryContainer = IceFrost,
    secondary = IceAccent,
    onSecondary = IceOnAccent,
    secondaryContainer = Color(0xFF004A77),
    onSecondaryContainer = Color(0xFFCDE8F5),
    tertiary = IceSteel,
    onTertiary = IceOnPrimary,
    tertiaryContainer = Color(0xFF37515E),
    onTertiaryContainer = Color(0xFFBDD8E4),
    error = IceDanger,
    onError = IceOnDanger,
    errorContainer = Color(0xFF7A1010),
    onErrorContainer = Color(0xFFFFB4AB),
    background = IceBackground,
    onBackground = IceOnBackground,
    surface = IceSurface,
    onSurface = IceOnSurface,
    surfaceVariant = IceSurfaceVariant,
    onSurfaceVariant = IceOnSurfaceVariant,
    outline = Color(0xFF4A7A96),
    outlineVariant = Color(0xFF1C3A52),
    scrim = Color(0xFF000000),
    inverseSurface = IceFrost,
    inverseOnSurface = IceDeepNavy,
    inversePrimary = IcePrimaryLight,
)

// Light color scheme
val IceLightColorScheme = lightColorScheme(
    primary = IcePrimaryLight,
    onPrimary = IceOnPrimaryLight,
    primaryContainer = Color(0xFFD0E8FF),
    onPrimaryContainer = Color(0xFF001D36),
    secondary = Color(0xFF00838F),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFB2EBF2),
    onSecondaryContainer = Color(0xFF003E40),
    tertiary = Color(0xFF4A6572),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFCDE8F5),
    onTertiaryContainer = Color(0xFF061F29),
    error = IceDanger,
    onError = IceOnDanger,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = IceBackgroundLight,
    onBackground = IceOnBackgroundLight,
    surface = IceSurfaceLight,
    onSurface = IceOnSurfaceLight,
    surfaceVariant = IceSurfaceVariantLight,
    onSurfaceVariant = IceOnSurfaceVariantLight,
    outline = Color(0xFF4A7A96),
    outlineVariant = Color(0xFFBDD8E4),
    scrim = Color(0xFF000000),
    inverseSurface = IcePrimary,
    inverseOnSurface = IceFrost,
    inversePrimary = Color(0xFF9ECAFF),
)

/**
 * Theme color extensions for consistent usage across screens
 */
object IceThemeColors {
    /** Primary cyan accent - for highlights, icons, active states */
    val cyan: Color @Composable get() = IceCyan

    /** Light cyan - for secondary highlights, hints */
    val cyanLight: Color @Composable get() = IceCyanLight

    /** Dark cyan - for pressed states, gradients */
    val cyanDark: Color @Composable get() = IceCyanDark

    /** Frost white - for important text on dark backgrounds */
    val frost: Color @Composable get() = IceFrost

    /** Star/achievement gold */
    val gold: Color @Composable get() = IceAccent

    /** Success green */
    val success: Color @Composable get() = IceSuccess

    /** Warning yellow */
    val warning: Color @Composable get() = IceWarning

    /** Danger red */
    val danger: Color @Composable get() = IceDanger

    /** Semi-transparent variants for overlays */
    val scrim: Color @Composable get() = Color.Black.copy(alpha = 0.5f)

    /** Card border gradient start */
    val borderStart: Color @Composable get() = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)

    /** Card border gradient end */
    val borderEnd: Color @Composable get() = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f)

    /** Card background gradient start */
    val cardBgStart: Color @Composable get() = IceSurface.copy(alpha = 0.65f)

    /** Card background gradient end */
    val cardBgEnd: Color @Composable get() = IceSurfaceVariant.copy(alpha = 0.45f)

    /** Secondary text color - cyan tinted for highlights */
    val textSecondary: Color @Composable get() = IceCyanLight

    /** Muted text color - slightly dimmed but still readable */
    val textMuted: Color @Composable get() = IceOnSurfaceVariant

    /** Disabled text color */
    val textDisabled: Color @Composable get() = IceOnSurfaceVariant.copy(alpha = 0.6f)
}

/**
 * Extended MaterialTheme with ice fishing colors
 */
val MaterialTheme.iceColors: IceThemeColors
    @Composable get() = IceThemeColors
