package random_photos_component.integration

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import utils.NasaPhotoUiModel
import kotlinx.coroutines.launch
import random_photos_component.data.RandomPhotosRepository
import random_photos_component.data.RandomPhotosResult
import random_photos_component.integration.RandomPhotosStore.LoadingState.*
import utils.StringMessage

interface RandomPhotosStore : Store<RandomPhotosStore.Intent, RandomPhotosStore.State, RandomPhotosStore.Label> {

    sealed interface Intent {
        data object LoadRandom : Intent
        data class ToggleSave(val photo: NasaPhotoUiModel) : Intent
        data object OpenDetails : Intent
        data object ResetLoadingState: Intent
    }

    data class State(
        val photos: List<NasaPhotoUiModel> = emptyList(),
        val savedIds: Set<String> = emptySet(),
        val loadingState: LoadingState = LoadingState.Initial
    )

    sealed interface LoadingState {
        data object Initial : LoadingState
        data object Loading : LoadingState
        data class Error(val error: StringMessage) : LoadingState
    }

    sealed interface Label {
        data object OpenDetails : Label
    }
}

class RandomPhotosStoreFactory(
    private val storeFactory: StoreFactory,
    private val randomPhotosRepository: RandomPhotosRepository
) {

    fun create(): RandomPhotosStore =
        object : RandomPhotosStore, Store<RandomPhotosStore.Intent, RandomPhotosStore.State, RandomPhotosStore.Label> by storeFactory.create(
            name = "RandomPhotosStore",
            initialState = RandomPhotosStore.State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object Init : Action
    }

    private sealed interface Msg {
        data object Loading : Msg
        data class PhotosLoaded(val photos: List<NasaPhotoUiModel>) : Msg
        data class SavedIdsUpdated(val savedIds: Set<String>) : Msg
        data class Error(val message: StringMessage) : Msg
        data object ResetLoadingState: Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.Init)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<RandomPhotosStore.Intent, Action, RandomPhotosStore.State, Msg, RandomPhotosStore.Label>() {
        override fun executeAction(action: Action) {
            when (action) {
                Action.Init -> {
                    observeSaved()
                    loadRandom()
                }
            }
        }

        override fun executeIntent(intent: RandomPhotosStore.Intent) {
            when (intent) {
                RandomPhotosStore.Intent.LoadRandom -> loadRandom()
                is RandomPhotosStore.Intent.ToggleSave -> toggleSave(intent.photo)
                RandomPhotosStore.Intent.OpenDetails -> publish(RandomPhotosStore.Label.OpenDetails)
                RandomPhotosStore.Intent.ResetLoadingState -> dispatch(Msg.ResetLoadingState)
            }
        }

        private fun observeSaved() {
            scope.launch {
                randomPhotosRepository.observeSavedPhotos().collect { savedPhotos ->
                    val ids = savedPhotos.map { it.id }.toSet()
                    dispatch(Msg.SavedIdsUpdated(ids))
                }
            }
        }

        private fun loadRandom() {
            dispatch(Msg.Loading)
            scope.launch {
                when (val result = randomPhotosRepository.getRandomPhotos(10)) {
                    is RandomPhotosResult.Error -> dispatch(Msg.Error(result.error))
                    is RandomPhotosResult.Success -> dispatch(Msg.PhotosLoaded(result.photoList))
                }
            }
        }

        private fun toggleSave(photo: NasaPhotoUiModel) {
            scope.launch {
                if (state().savedIds.contains(photo.id)) {
                    randomPhotosRepository.deletePhoto(photo.id)
                } else {
                    randomPhotosRepository.savePhoto(photo)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<RandomPhotosStore.State, Msg> {
        override fun RandomPhotosStore.State.reduce(msg: Msg): RandomPhotosStore.State =
            when (msg) {
                Msg.Loading ->  copy(loadingState = Loading)
                is Msg.PhotosLoaded -> copy(photos = msg.photos, loadingState = RandomPhotosStore.LoadingState.Initial)
                is Msg.SavedIdsUpdated -> copy(savedIds = msg.savedIds)
                is Msg.Error -> copy(loadingState = Error(msg.message))
                Msg.ResetLoadingState -> copy(loadingState = Initial)
            }
    }
}
