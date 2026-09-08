package root_component.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import photo_details_component.PhotoDetails
import photo_details_component.PhotoDetailsComponent
import utils.NasaPhotoUiModel
import kotlinx.serialization.Serializable
import main_tabs_component.MainTabsComponent

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val mainTabsComponentFactory: MainTabsComponent.Factory,
    private val photoDetailsComponentFactory: PhotoDetailsComponent.Factory
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.MainTabs,
        handleBackButton = true,
        childFactory = ::child
    )

    override fun onClickBack() {
        navigation.pop()
    }

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child {
        return when (config) {
            Config.MainTabs -> {
                val component = mainTabsComponentFactory(
                    componentContext = componentContext,
                    onOpenPhotoDetails = ::openPhotoDetails
                )
                RootComponent.Child.MainTabs(component)
            }

            is Config.PhotoDetailsConfig -> {
                val component = photoDetailsComponentFactory(
                    componentContext = componentContext,
                    photoDetails = config.details,
                    onBack = { onClickBack() }
                )
                RootComponent.Child.PhotoDetails(component)
            }
        }
    }

    @OptIn(DelicateDecomposeApi::class)
    private fun openPhotoDetails(photo: NasaPhotoUiModel) {
        navigation.push(
            Config.PhotoDetailsConfig(
                details = PhotoDetails(
                    title = photo.title,
                    explanation = photo.explanation,
                    imageUrl = photo.hdurl ?: photo.url,
                    date = photo.date,
                    copyright = photo.copyright,
                    mediaType = photo.mediaType
                )
            )
        )
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object MainTabs : Config

        @Serializable
        data class PhotoDetailsConfig(
            val details: PhotoDetails
        ) : Config
    }

    class Factory(
        private val mainTabsComponentFactory: MainTabsComponent.Factory,
        private val photoDetailsComponentFactory: PhotoDetailsComponent.Factory
    ) : RootComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): RootComponent {
            return DefaultRootComponent(
                componentContext = componentContext,
                mainTabsComponentFactory = mainTabsComponentFactory,
                photoDetailsComponentFactory = photoDetailsComponentFactory
            )
        }
    }
}
