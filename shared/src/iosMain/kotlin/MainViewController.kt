import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.arkivanov.essenty.backhandler.BackDispatcher
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.UIKit.UIViewController
import root_component.RootContent
import root_component.integration.RootComponent

@OptIn(ExperimentalDecomposeApi::class)
fun MainViewController(rootComponent: RootComponent, backDispatcher: BackDispatcher,): UIViewController {

    return ComposeUIViewController {
        PredictiveBackGestureOverlay(
            endEdgeEnabled = false,
            edgeWidth = 50.dp,
            backDispatcher = backDispatcher,
            backIcon = { progress, _ -> },
            modifier = Modifier.fillMaxSize(),
        ) {
            RootContent(component = rootComponent)
        }

    }
}


class KoinHelper : KoinComponent {
    private val rootComponentFactory: RootComponent.Factory by inject()
    fun getRootComponentFactory(): RootComponent.Factory = rootComponentFactory
}