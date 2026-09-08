package saved_photos_component

import com.arkivanov.decompose.ComponentContext
import utils.NasaPhotoUiModel
import kotlinx.coroutines.flow.StateFlow
import saved_photos_component.integration.SavedPhotosStore

interface SavedPhotosComponent {
    val model: StateFlow<SavedPhotosStore.State>
    fun toggleSavePhoto(photo: NasaPhotoUiModel)
    fun openPhotoDetail(photo: NasaPhotoUiModel?)

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): SavedPhotosComponent
    }
}
