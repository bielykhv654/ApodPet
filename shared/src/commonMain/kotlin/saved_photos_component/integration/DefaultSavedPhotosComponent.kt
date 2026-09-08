package saved_photos_component.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import utils.NasaPhotoUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import saved_photos_component.SavedPhotosComponent

class DefaultSavedPhotosComponent(
    componentContext: ComponentContext,
    savedPhotosStoreFactory: SavedPhotosStoreFactory
) : SavedPhotosComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { savedPhotosStoreFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SavedPhotosStore.State>
        get() = store.stateFlow

    override fun toggleSavePhoto(photo: NasaPhotoUiModel) {
        store.accept(SavedPhotosStore.Intent.ToggleSavePhoto(photo))
    }

    override fun openPhotoDetail(photo: NasaPhotoUiModel?) {
        store.accept(SavedPhotosStore.Intent.OpenPhotoDetail(photo))
    }

    class Factory(
        private val savedPhotosStoreFactory: SavedPhotosStoreFactory
    ) : SavedPhotosComponent.Factory {
        override fun invoke(componentContext: ComponentContext): SavedPhotosComponent {
            return DefaultSavedPhotosComponent(
                componentContext = componentContext,
                savedPhotosStoreFactory = savedPhotosStoreFactory
            )
        }
    }
}