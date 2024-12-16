package com.newsapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val PrimaryColor = Color(0xFF405DE6)
private val SecondaryColor = Color(0xFF833AB4) // Gradient purple
private val AccentColor = Color(0xFFFD1D1D)
private val LightBackground = Color(0xFFFFFFFF)
private val DarkBackground = Color(0xFF000000)  // Dark background for dark mode
private val SurfaceLight = Color(0xFFF8F8F8)    // Light gray surface for light mode
private val SurfaceDark = Color(0xFF121212)     // Dark surface for dark mode
private val OnPrimaryLight = Color(0xFFFFFFFF)  // White text on primary in light mode
private val OnPrimaryDark = Color(0xFF000000)   // Black text on primary in dark mode
private val TextLight = Color(0xFF000000)       // Black text for light mode
private val TextDark = Color(0xFFFFFFFF)        // White text for dark mode

// Define the dark color scheme
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = AccentColor,
    background = DarkBackground,
    surface = SurfaceDark,
    onPrimary = OnPrimaryDark,
    onSecondary = OnPrimaryDark,
    onTertiary = OnPrimaryDark,
    onBackground = TextDark,
    onSurface = TextDark
)

// Define the light color scheme
private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = AccentColor,
    background = LightBackground,
    surface = SurfaceLight,
    onPrimary = OnPrimaryLight,
    onSecondary = OnPrimaryLight,
    onTertiary = OnPrimaryLight,
    onBackground = TextLight,
    onSurface = TextLight
)

@Composable
fun NewsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
