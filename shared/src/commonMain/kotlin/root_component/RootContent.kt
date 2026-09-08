package root_component

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import common_ui.backAnimation
import photo_details_component.PhotoDetailsScreen
import main_tabs_component.MainTabsScreen
import root_component.integration.RootComponent
import ui.theme.AppTheme

@Composable
fun RootContent(component: RootComponent) {
    AppTheme {
        Children(
            animation = backAnimation(
                backHandler = component.backHandler,
                onBack = component::onClickBack,
            ),
            stack = component.stack) {
            when (val instance = it.instance) {
                is RootComponent.Child.MainTabs -> MainTabsScreen(instance.component)
                is RootComponent.Child.PhotoDetails -> PhotoDetailsScreen(instance.component)
            }
        }
    }
}
