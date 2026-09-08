package common_ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
actual fun BottomBar(
    tabs: List<BottomTabItem>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        tabs.forEachIndexed { index, tab ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                icon = { Text(tab.iconText, fontSize = 20.sp) },
                label = { Text(tab.title) }
            )
        }
    }
}

actual fun is26Ios(): Boolean = false