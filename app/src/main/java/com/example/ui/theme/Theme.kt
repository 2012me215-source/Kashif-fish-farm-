package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF2DD4BF), // Glowing clean seafoam teal
    secondary = Color(0xFF0D9488),
    tertiary = Color(0xFF38BDF8),
    background = AquaticDarkBackground,
    surface = AquaticDarkSurface,
    primaryContainer = Color(0xFF115E59),
    onPrimaryContainer = Color(0xFFCCFBF1),
    secondaryContainer = Color(0xFF134E4A),
    onSecondaryContainer = Color(0xFFCCFBF1),
    tertiaryContainer = Color(0xFF0C4A6E),
    onTertiaryContainer = Color(0xFFE0F2FE),
    onPrimary = Color(0xFF042F2E),
    onSecondary = Color.White,
    onTertiary = Color(0xFF082F49),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF475569)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = TealPrimary, // Soothing Dark Teal
    secondary = TealSecondary, // Deep Oceanic Forest Teal
    tertiary = TealTertiary,
    background = AquaticLightBackground, // Soft Slate off-white background
    surface = AquaticLightSurface, // Pure crisp white cards
    primaryContainer = Color(0xFFE0F2F1), // Soothing fresh mint-cream water
    onPrimaryContainer = Color(0xFF0F766E),
    secondaryContainer = Color(0xFFCCFBF1), // Extremely pleasing seafoam navigation active pill
    onSecondaryContainer = Color(0xFF115E59),
    tertiaryContainer = Color(0xFFE0F2FE), // Calm sky-river soft container
    onTertiaryContainer = Color(0xFF0369A1),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9), // Slate tinted item background
    onSurfaceVariant = Color(0xFF475569), // Pleasing slate text hierarchy
    outline = Color(0xFFCBD5E1) // Soft border lines (no harsh contrast)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
