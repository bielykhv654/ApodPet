package photo_details_component

import com.arkivanov.decompose.ComponentContext

class DefaultPhotoDetailsComponent(
    componentContext: ComponentContext,
    override val photoDetails: PhotoDetails,
    private val onBackAction: () -> Unit
) : PhotoDetailsComponent, ComponentContext by componentContext {

    override fun onBack() {
        onBackAction()
    }

    class Factory : PhotoDetailsComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            photoDetails: PhotoDetails,
            onBack: () -> Unit
        ): PhotoDetailsComponent {
            return DefaultPhotoDetailsComponent(
                componentContext = componentContext,
                photoDetails = photoDetails,
                onBackAction = onBack
            )
        }
    }
}
