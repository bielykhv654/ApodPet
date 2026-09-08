package common_ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class BottomTabItem(
    val title: String,
    val iosIcon: String,
    val iconText: String
)

@Composable
expect fun BottomBar(
    tabs: List<BottomTabItem>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
)

expect fun is26Ios(): Boolean