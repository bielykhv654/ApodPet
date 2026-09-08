package root_component.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import photo_details_component.PhotoDetailsComponent
import main_tabs_component.MainTabsComponent

interface RootComponent:BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>
    fun onClickBack()
    sealed class Child {
        data class MainTabs(val component: MainTabsComponent) : Child()
        data class PhotoDetails(val component: PhotoDetailsComponent) : Child()
    }

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): RootComponent
    }
}
