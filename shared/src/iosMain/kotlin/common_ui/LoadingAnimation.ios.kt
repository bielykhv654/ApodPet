package common_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import com.multiplatform.webview.util.toUIColor
import platform.UIKit.UIActivityIndicatorView
import platform.UIKit.UIActivityIndicatorViewStyleMedium

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun LoadingAnimation() {
  val  color = MaterialTheme.colorScheme.onSurfaceVariant
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        UIKitView(
            properties = UIKitInteropProperties(placedAsOverlay = true),
            factory = {
                val indicator = UIActivityIndicatorView()
                indicator.activityIndicatorViewStyle = UIActivityIndicatorViewStyleMedium
                indicator.startAnimating()
                indicator.color = color.toUIColor()
                indicator
            },
            modifier = Modifier.padding(bottom = 10.dp)
        )
    }
}