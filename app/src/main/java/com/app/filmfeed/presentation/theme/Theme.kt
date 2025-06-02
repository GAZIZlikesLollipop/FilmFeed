package com.app.filmfeed.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

// Локальный провайдер для кастомных цветов (если нужно добавить дополнительные)
val LocalFilmFeedColors = staticCompositionLocalOf { LightColors }

@Composable
fun FilmFeedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(LocalFilmFeedColors provides colorScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = FilmFeedTypography,
            content = content
        )
    }
}
