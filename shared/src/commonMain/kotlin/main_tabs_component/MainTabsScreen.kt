package main_tabs_component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import apodpet.shared.generated.resources.Res
import apodpet.shared.generated.resources.action_refresh
import apodpet.shared.generated.resources.tab_random
import apodpet.shared.generated.resources.tab_random_title
import apodpet.shared.generated.resources.tab_range
import apodpet.shared.generated.resources.tab_range_title
import apodpet.shared.generated.resources.tab_saved
import apodpet.shared.generated.resources.tab_saved_title
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import common_ui.BottomBar
import common_ui.BottomTabItem
import common_ui.SpaceBackground
import common_ui.is26Ios
import org.jetbrains.compose.resources.stringResource
import random_photos_component.RandomPhotosScreen
import date_range_component.RangePhotosScreen
import saved_photos_component.SavedPhotosScreen
import ui.theme.LocalThemeController

private const val RANDOM = 0
private const val RANGE = 1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabsScreen(component: MainTabsComponent) {
    val selectedTab by component.selectedTab.subscribeAsState()
    val themeController = LocalThemeController.current

    val tabRandomTitle = stringResource(Res.string.tab_random_title)
    val tabRangeTitle = stringResource(Res.string.tab_range_title)
    val tabSavedTitle = stringResource(Res.string.tab_saved_title)
    val tabRandomLabel = stringResource(Res.string.tab_random)
    val tabRangeLabel = stringResource(Res.string.tab_range)
    val tabSavedLabel = stringResource(Res.string.tab_saved)
    val refreshText = stringResource(Res.string.action_refresh)

    val tabs = remember(tabRandomLabel, tabRangeLabel, tabSavedLabel) {
        listOf(
            BottomTabItem(title = tabRandomLabel, iosIcon = "die.face.5", iconText = "🎲"),
            BottomTabItem(title = tabRangeLabel, iosIcon = "calendar", iconText = "📅"),
            BottomTabItem(title = tabSavedLabel, iosIcon = "heart", iconText = "❤️")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedTab) {
                            RANDOM -> tabRandomTitle
                            RANGE -> tabRangeTitle
                            else -> tabSavedTitle
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (selectedTab == RANDOM) {
                        IconButton(onClick = { themeController.toggleTheme() }) {
                            Text(if (themeController.isDark) "☀️" else "🌙", fontSize = 20.sp)
                        }
                        TextButton(onClick = { component.randomPhotosComponent.loadRandom() }) {
                            Text(refreshText, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    } else if (selectedTab == RANGE) {
                        TextButton(onClick = { component.rangePhotosComponent.refresh() }) {
                            Text(refreshText, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            if (!is26Ios())
                BottomBar(
                    tabs = tabs,
                    selectedTabIndex = selectedTab,
                    onTabSelected = { component.selectTab(it) }
                )
        }
    ) { paddingValues ->
        SpaceBackground(modifier = Modifier.padding(paddingValues)) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (selectedTab) {
                    RANDOM -> RandomPhotosScreen(
                        component = component.randomPhotosComponent,
                        onOpenDetail = component::openPhotoDetails
                    )
                    RANGE -> RangePhotosScreen(
                        component = component.rangePhotosComponent,
                        onOpenDetail = component::openPhotoDetails
                    )
                    else -> SavedPhotosScreen(
                        component = component.savedPhotosComponent,
                        onOpenDetail = component::openPhotoDetails
                    )
                }

                if (is26Ios())
                    BottomBar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        tabs = tabs,
                        selectedTabIndex = selectedTab,
                        onTabSelected = { component.selectTab(it) }
                    )
            }
        }
    }
}
