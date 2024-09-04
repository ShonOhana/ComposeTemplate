package com.example.composetemplate.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val LocalCustomTypography = staticCompositionLocalOf {
    CustomTypography()
}

private val localTypography = CustomTypography(
    title = TitleStyle
)

private val LocalCustomColorsPalette = staticCompositionLocalOf {
    CustomColorsPalette()
}

private val LightCustomColorsPalette = CustomColorsPalette(
    title = Orange
)

private val DarkCustomColorsPalette = CustomColorsPalette(
    title = White
)

@Composable
fun ComposeTemplateTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    /* Dynamic color is available on Android 12+ */
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (isDarkMode) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        isDarkMode -> DarkColorScheme
        else -> LightColorScheme
    }
    val localCustomColorScheme = if (isDarkMode) {
        DarkCustomColorsPalette
    } else {
        LightCustomColorsPalette
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        /* Paint the status bar */
        SideEffect {
            ((view.context as? Activity)?.window)?.let { window ->
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                    isDarkMode
            }
        }
    }

    CompositionLocalProvider(
        LocalCustomColorsPalette provides localCustomColorScheme,
        LocalCustomTypography provides localTypography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = shapes,
            content = content
        )
    }
}

object CustomTheme {
    val colors: CustomColorsPalette
        @Composable get() = LocalCustomColorsPalette.current

    val typography: CustomTypography
        @Composable get() = LocalCustomTypography.current
}