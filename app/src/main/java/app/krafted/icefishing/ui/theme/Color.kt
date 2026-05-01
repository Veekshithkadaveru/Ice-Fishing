package app.krafted.icefishing.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val IcePrimary = Color(0xFF1A3A5C)
val IcePrimaryVariant = Color(0xFF0D2238)
val IceOnPrimary = Color(0xFFE8F4FD)
val IceSecondary = Color(0xFF2E6DA4)
val IceOnSecondary = Color(0xFFFFFFFF)
val IceBackground = Color(0xFF0A1929)
val IceOnBackground = Color(0xFFDCEEF8)
val IceSurface = Color(0xFF0F2A40)
val IceSurfaceVariant = Color(0xFF1C3A52)
val IceOnSurface = Color(0xFFCAE4F5)
val IceOnSurfaceVariant = Color(0xFF90BACD)
val IceAccent = Color(0xFFFFB300)
val IceOnAccent = Color(0xFF1A1200)
val IceDanger = Color(0xFFCF3030)
val IceOnDanger = Color(0xFFFFFFFF)
val IceFrost = Color(0xFFE8F4FD)
val IceSteel = Color(0xFF607D8B)
val IceDeepNavy = Color(0xFF061220)
val IceMidnight = Color(0xFF030C17)

val IcePrimaryLight = Color(0xFF1E5FA8)
val IceOnPrimaryLight = Color(0xFFFFFFFF)
val IceBackgroundLight = Color(0xFFF0F7FF)
val IceOnBackgroundLight = Color(0xFF0A1929)
val IceSurfaceLight = Color(0xFFFFFFFF)
val IceSurfaceVariantLight = Color(0xFFDCEEF8)
val IceOnSurfaceLight = Color(0xFF0D2238)
val IceOnSurfaceVariantLight = Color(0xFF2E6DA4)

val IceDarkColorScheme = darkColorScheme(
    primary = IceSecondary,
    onPrimary = IceOnPrimary,
    primaryContainer = IcePrimary,
    onPrimaryContainer = IceFrost,
    secondary = IceAccent,
    onSecondary = IceOnAccent,
    secondaryContainer = Color(0xFF5C3D00),
    onSecondaryContainer = Color(0xFFFFD966),
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

val IceLightColorScheme = lightColorScheme(
    primary = IcePrimaryLight,
    onPrimary = IceOnPrimaryLight,
    primaryContainer = Color(0xFFD0E8FF),
    onPrimaryContainer = Color(0xFF001D36),
    secondary = Color(0xFF8A6400),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDFA0),
    onSecondaryContainer = Color(0xFF291B00),
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
