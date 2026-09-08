package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Единый глобальный объект состояния темы
object ThemeState {
    private var userIsDark: Boolean? by mutableStateOf(null)

    fun isDark(systemIsDark: Boolean): Boolean {
        return userIsDark ?: systemIsDark
    }

    fun toggleTheme(systemIsDark: Boolean) {
        val current = userIsDark ?: systemIsDark
        userIsDark = !current
    }
}

class ThemeController(
    val isDark: Boolean,
    private val onToggle: () -> Unit
) {
    fun toggleTheme() {
        onToggle()
    }
}

val LocalThemeController = staticCompositionLocalOf {
    ThemeController(isDark = false, onToggle = {})
}

// Dark Space Theme
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA5B4FC),
    onPrimary = Color(0xFF1E1B4B),
    primaryContainer = Color(0xFF1E234A),
    onPrimaryContainer = Color(0xFFA5B4FC),
    secondary = Color(0xFFC7D2FE),
    secondaryContainer = Color(0xFF1E234A),
    onSecondaryContainer = Color(0xFFC7D2FE),
    background = Color(0xFF090A12),
    onBackground = Color.White,
    surface = Color(0xFF0D0F1F),
    onSurface = Color.White,
    surfaceContainerHigh = Color(0xFF16192E),
    onSurfaceVariant = Color(0xFFD0D3F5)
)

// Light Cosmic Theme
val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3F51B5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8EAF6),
    onPrimaryContainer = Color(0xFF1A237E),
    secondary = Color(0xFF5C6BC0),
    secondaryContainer = Color(0xFFE8EAF6),
    onSecondaryContainer = Color(0xFF283593),
    background = Color(0xFFF5F6FC),
    onBackground = Color(0xFF1A1C2E),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1C2E),
    surfaceContainerHigh = Color(0xFFFFFFFF),
    onSurfaceVariant = Color(0xFF4A4E69)
)

@Composable
fun AppTheme(
    systemIsDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val isDark = ThemeState.isDark(systemIsDark)

    val themeController = ThemeController(
        isDark = isDark,
        onToggle = { ThemeState.toggleTheme(systemIsDark) }
    )

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(LocalThemeController provides themeController) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
