package date_range_component

import com.arkivanov.decompose.ComponentContext
import utils.NasaPhotoUiModel
import kotlinx.coroutines.flow.StateFlow
import date_range_component.integration.RangePhotosStore

interface RangePhotosComponent {
    val model: StateFlow<RangePhotosStore.State>
    fun toggleSave(photo: NasaPhotoUiModel)
    fun refresh()
    fun resetLoadingState()

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): RangePhotosComponent
    }
}
