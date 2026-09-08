package random_photos_component

import com.arkivanov.decompose.ComponentContext
import utils.NasaPhotoUiModel
import kotlinx.coroutines.flow.StateFlow
import random_photos_component.integration.RandomPhotosStore

interface RandomPhotosComponent {
    val model: StateFlow<RandomPhotosStore.State>
    fun loadRandom()
    fun toggleSave(photo: NasaPhotoUiModel)
    fun resetLoadingState()

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): RandomPhotosComponent
    }
}
