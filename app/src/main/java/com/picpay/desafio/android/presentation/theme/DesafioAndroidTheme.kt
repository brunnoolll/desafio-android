package com.picpay.desafio.android.presentation.theme


import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ColorAccent,
    background = ColorPrimaryDark,
    surface = ColorPrimary,
    onPrimary = Color.White,
    onBackground = Color.White,
    onSurface = ColorPrimaryLight
)

private val LightColorScheme = lightColorScheme(
    primary = ColorAccent,
    background = Color.White,
    surface = Color(0xFFF0F1F2),
    onPrimary = Color.White,
    onBackground = ColorPrimaryDark,
    onSurface = ColorPrimary
)

@Composable
fun DesafioAndroidTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}