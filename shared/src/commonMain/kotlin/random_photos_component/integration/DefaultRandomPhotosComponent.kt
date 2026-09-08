package random_photos_component.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import utils.NasaPhotoUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import random_photos_component.RandomPhotosComponent

class DefaultRandomPhotosComponent(
    componentContext: ComponentContext,
    randomPhotosStoreFactory: RandomPhotosStoreFactory
) : RandomPhotosComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { randomPhotosStoreFactory.create() }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<RandomPhotosStore.State>
        get() = store.stateFlow

    override fun loadRandom() {
        store.accept(RandomPhotosStore.Intent.LoadRandom)
    }

    override fun toggleSave(photo: NasaPhotoUiModel) {
        store.accept(RandomPhotosStore.Intent.ToggleSave(photo))
    }

    override fun resetLoadingState() {
        store.accept(RandomPhotosStore.Intent.ResetLoadingState)

    }

    class Factory(
        private val randomPhotosStoreFactory: RandomPhotosStoreFactory
    ) : RandomPhotosComponent.Factory {
        override fun invoke(componentContext: ComponentContext): RandomPhotosComponent {
            return DefaultRandomPhotosComponent(
                componentContext = componentContext,
                randomPhotosStoreFactory = randomPhotosStoreFactory
            )
        }
    }
}
