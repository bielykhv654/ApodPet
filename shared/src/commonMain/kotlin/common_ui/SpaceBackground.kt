package common_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import ui.theme.LocalThemeController

@Composable
fun SpaceBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isDark = LocalThemeController.current.isDark
    val bgModifier = if (isDark) {
        Modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0D0E15),
                    Color(0xFF1A1B2F),
                    Color(0xFF101225)
                )
            )
        )
    } else {
        Modifier.background(MaterialTheme.colorScheme.background)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(bgModifier)
    ) {
        content()
    }
}
