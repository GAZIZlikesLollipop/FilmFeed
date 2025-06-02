package com.app.filmfeed.presentation.theme

import androidx.compose.ui.graphics.Color

val FilmFeedBlack = Color(0xFF121212) // Глубокий чёрный для фона
val FilmFeedDarkGray = Color(0xFF1C2526) // Тёмно-серый для вторичных элементов
val FilmFeedRed = Color(0xFFE50914) // Яркий красный, вдохновлённый неоновыми вывесками
val FilmFeedGold = Color(0xFFD4A017) // Золотой для акцентов (рейтинги, кнопки)
val FilmFeedWhite = Color(0xFFF5F5F5) // Светлый для текста на тёмном фоне

// Светлая палитра (для светлой темы)
val LightColors = androidx.compose.material3.lightColorScheme(
    primary = FilmFeedRed,
    onPrimary = FilmFeedWhite,
    primaryContainer = FilmFeedRed.copy(alpha = 0.1f),
    onPrimaryContainer = FilmFeedWhite,
    secondary = FilmFeedGold,
    onSecondary = FilmFeedBlack,
    secondaryContainer = FilmFeedGold.copy(alpha = 0.1f),
    onSecondaryContainer = FilmFeedBlack,
    background = FilmFeedWhite,
    onBackground = FilmFeedBlack,
    surface = FilmFeedWhite,
    onSurface = FilmFeedBlack,
    error = Color(0xFFB00020),
    onError = FilmFeedWhite
)

// Тёмная палитра (для тёмной темы)
val DarkColors = androidx.compose.material3.darkColorScheme(
    primary = FilmFeedRed,
    onPrimary = FilmFeedWhite,
    primaryContainer = FilmFeedRed.copy(alpha = 0.2f),
    onPrimaryContainer = FilmFeedWhite,
    secondary = FilmFeedGold,
    onSecondary = FilmFeedBlack,
    secondaryContainer = FilmFeedGold.copy(alpha = 0.2f),
    onSecondaryContainer = FilmFeedWhite,
    background = FilmFeedBlack,
    onBackground = FilmFeedWhite,
    surface = FilmFeedDarkGray,
    onSurface = FilmFeedWhite,
    error = Color(0xFFCF6679),
    onError = FilmFeedBlack
)
