package common_ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import androidx.compose.material3.MaterialTheme
import com.multiplatform.webview.util.toUIColor
import platform.UIKit.UIDevice
import platform.UIKit.UIImage
import platform.UIKit.UITabBar
import platform.UIKit.UITabBarDelegateProtocol
import platform.UIKit.UITabBarItem
import platform.darwin.NSObject

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun BottomBar(
    tabs: List<BottomTabItem>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier
) {
    val tabBar = remember { UITabBar() }
    val delegate = remember(onTabSelected) {
        object : NSObject(), UITabBarDelegateProtocol {
            override fun tabBar(tabBar: UITabBar, didSelectItem: UITabBarItem) {
                val index = tabBar.items?.indexOf(didSelectItem) ?: -1
                if (index != -1) {
                    onTabSelected(index)
                }
            }
        }
    }
    tabBar.tintColor = MaterialTheme.colorScheme.onSurfaceVariant.toUIColor()
    // Setup tab bar items
    LaunchedEffect(tabs) {
        val tabBarItems = tabs.mapIndexed { index, tab ->
            UITabBarItem(
                title = tab.title,
                image = UIImage.systemImageNamed(tab.iosIcon),
                tag = index.toLong()
            )
        }
        tabBar.setItems(tabBarItems)
        tabBar.delegate = delegate
    }

    // Update selected tab
    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex in 0 until (tabBar.items?.size ?: 0)) {
            tabBar.selectedItem = tabBar.items?.get(selectedTabIndex) as? UITabBarItem
        }
    }

    UIKitView(
        properties = UIKitInteropProperties(placedAsOverlay = is26Ios()),
        factory = { tabBar },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = if (is26Ios()) 0.dp else 40.dp)
    )
}


actual fun is26Ios(): Boolean {
    val version = UIDevice.currentDevice.systemVersion
        .substringBefore(".")
        .toIntOrNull()
    println(version)
    return (version ?: 0) >= 26
}
