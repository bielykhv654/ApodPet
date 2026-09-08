package date_range_component.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import utils.NasaPhotoUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import date_range_component.RangePhotosComponent

class DefaultRangePhotosComponent(
    componentContext: ComponentContext,
    rangePhotosStoreFactory: RangePhotosStoreFactory
) : RangePhotosComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { rangePhotosStoreFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<RangePhotosStore.State>
        get() = store.stateFlow

    override fun toggleSave(photo: NasaPhotoUiModel) {
        store.accept(RangePhotosStore.Intent.ToggleSave(photo))
    }

    override fun refresh() {
        store.accept(RangePhotosStore.Intent.Refresh)
    }

    override fun resetLoadingState() {
        store.accept(RangePhotosStore.Intent.ResetLoadingState)
    }

    class Factory(
        private val rangePhotosStoreFactory: RangePhotosStoreFactory
    ) : RangePhotosComponent.Factory {
        override fun invoke(componentContext: ComponentContext): RangePhotosComponent {
            return DefaultRangePhotosComponent(
                componentContext = componentContext,
                rangePhotosStoreFactory = rangePhotosStoreFactory
            )
        }
    }
}
