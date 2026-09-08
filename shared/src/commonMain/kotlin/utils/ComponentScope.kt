package utils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

fun ComponentContext.componentScope(): CoroutineScope = CoroutineScope(
    AppCoroutineDispatcher.Main + SupervisorJob()
).apply {
    lifecycle.doOnDestroy { cancel() }
}

object AppCoroutineDispatcher {
     val IO: CoroutineDispatcher = Dispatchers.IO
    val Main: CoroutineDispatcher = Dispatchers.Main.immediate
}