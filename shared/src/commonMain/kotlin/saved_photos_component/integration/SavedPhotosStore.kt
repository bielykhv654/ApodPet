package saved_photos_component.integration

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import utils.NasaPhotoUiModel
import utils.toUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import saved_photos_component.data.SavedPhotosRepository

interface SavedPhotosStore : Store<SavedPhotosStore.Intent, SavedPhotosStore.State, SavedPhotosStore.Label> {
    sealed interface Intent {
        data class ToggleSavePhoto(val photo: NasaPhotoUiModel) : Intent
        data class OpenPhotoDetail(val photo: NasaPhotoUiModel?) : Intent
    }

    data class State(
        val savedPhotos: List<NasaPhotoUiModel> = emptyList(),
        val selectedPhotoDetail: NasaPhotoUiModel? = null
    )

    sealed interface Label
}

class SavedPhotosStoreFactory(
    private val storeFactory: StoreFactory,
    private val repository: SavedPhotosRepository
) {
    fun create(): SavedPhotosStore =
        object : SavedPhotosStore, Store<SavedPhotosStore.Intent, SavedPhotosStore.State, SavedPhotosStore.Label> by storeFactory.create(
            name = "SavedPhotosStore",
            initialState = SavedPhotosStore.State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class SavedPhotosUpdated(val list: List<NasaPhotoUiModel>) : Action
    }

    private sealed interface Msg {
        data class SavedPhotosUpdated(val list: List<NasaPhotoUiModel>) : Msg
        data class OpenPhotoDetail(val photo: NasaPhotoUiModel?) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                repository.observeSavedPhotos().collectLatest { entities ->
                    val uiModels = entities.map { it.toUiModel() }
                    dispatch(Action.SavedPhotosUpdated(uiModels))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<SavedPhotosStore.Intent, Action, SavedPhotosStore.State, Msg, SavedPhotosStore.Label>() {
        override fun executeAction(action: Action) {
            when (action) {
                is Action.SavedPhotosUpdated -> dispatch(Msg.SavedPhotosUpdated(action.list))
            }
        }

        override fun executeIntent(intent: SavedPhotosStore.Intent) {
            when (intent) {
                is SavedPhotosStore.Intent.ToggleSavePhoto -> toggleSavePhoto(intent.photo)
                is SavedPhotosStore.Intent.OpenPhotoDetail -> dispatch(Msg.OpenPhotoDetail(intent.photo))
            }
        }

        private fun toggleSavePhoto(photo: NasaPhotoUiModel) {
            scope.launch {
                repository.deletePhoto(photo.id)
            }
        }
    }

    private object ReducerImpl : Reducer<SavedPhotosStore.State, Msg> {
        override fun SavedPhotosStore.State.reduce(msg: Msg): SavedPhotosStore.State =
            when (msg) {
                is Msg.SavedPhotosUpdated -> copy(savedPhotos = msg.list)
                is Msg.OpenPhotoDetail -> copy(selectedPhotoDetail = msg.photo)
            }
    }
}
