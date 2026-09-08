package date_range_component.integration

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import utils.NasaPhotoUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import date_range_component.data.RangePhotosRepository
import date_range_component.integration.RangePhotosStore.LoadingState.*
import utils.StringMessage

interface RangePhotosStore : Store<RangePhotosStore.Intent, RangePhotosStore.State, RangePhotosStore.Label> {

    sealed interface Intent {
        data class ToggleSave(val photo: NasaPhotoUiModel) : Intent
        data object OpenDetails : Intent
        data object Refresh : Intent
        data object ResetLoadingState: Intent
    }

    data class State(
        val pagingDataFlow: Flow<PagingData<NasaPhotoUiModel>> = emptyFlow(),
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

class RangePhotosStoreFactory(
    private val storeFactory: StoreFactory,
    private val rangePhotosRepository: RangePhotosRepository
) {

    fun create(): RangePhotosStore =
        object : RangePhotosStore, Store<RangePhotosStore.Intent, RangePhotosStore.State, RangePhotosStore.Label> by storeFactory.create(
            name = "RangePhotosStore",
            initialState = RangePhotosStore.State(),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object Init : Action
    }

    private sealed interface Msg {
        data class PagingFlowCreated(val pagingDataFlow: Flow<PagingData<NasaPhotoUiModel>>) : Msg
        data class SavedIdsUpdated(val savedIds: Set<String>) : Msg
        data class Error(val error: StringMessage) : Msg
        data object Loading : Msg
        data object ResetLoadingState: Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            dispatch(Action.Init)
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<RangePhotosStore.Intent, Action, RangePhotosStore.State, Msg, RangePhotosStore.Label>() {

        private var currentPagingSource: PagingSource<String, NasaPhotoUiModel>? = null

        private val pagerFlow: Flow<PagingData<NasaPhotoUiModel>> by lazy {
            Pager(
                config = PagingConfig(
                    pageSize = 10,
                    initialLoadSize = 10,
                    prefetchDistance = 2,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    rangePhotosRepository.getPhotosPagingSource(
                        onError = { error ->
                            dispatch(Msg.Error(error))
                        }
                    ).also { currentPagingSource = it }
                }
            ).flow.cachedIn(scope)
        }

        override fun executeAction(action: Action) {
            when (action) {
                Action.Init -> {
                    dispatch(Msg.PagingFlowCreated(pagerFlow))
                    observeSaved()
                }
            }
        }

        override fun executeIntent(intent: RangePhotosStore.Intent) {
            when (intent) {
                is RangePhotosStore.Intent.ToggleSave -> toggleSave(intent.photo)
                RangePhotosStore.Intent.OpenDetails -> publish(RangePhotosStore.Label.OpenDetails)
                RangePhotosStore.Intent.Refresh -> {
                    currentPagingSource?.invalidate()
                }

                RangePhotosStore.Intent.ResetLoadingState -> dispatch(Msg.ResetLoadingState)
            }
        }

        private fun observeSaved() {
            scope.launch {
                rangePhotosRepository.observeSavedPhotos().collect { savedEntities ->
                    val ids = savedEntities.map { it.id }.toSet()
                    dispatch(Msg.SavedIdsUpdated(ids))
                }
            }
        }

        private fun toggleSave(photo: NasaPhotoUiModel) {
            scope.launch {
                if (state().savedIds.contains(photo.id)) {
                    rangePhotosRepository.deletePhoto(photo.id)
                } else {
                    rangePhotosRepository.savePhoto(photo)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<RangePhotosStore.State, Msg> {
        override fun RangePhotosStore.State.reduce(msg: Msg): RangePhotosStore.State =
            when (msg) {
                is Msg.PagingFlowCreated -> copy(pagingDataFlow = msg.pagingDataFlow)
                is Msg.SavedIdsUpdated -> copy(savedIds = msg.savedIds)
                is Msg.Error -> copy(loadingState = Error(msg.error))
                Msg.Loading -> copy(loadingState = Loading)
                Msg.ResetLoadingState -> copy(loadingState = Initial)
            }
    }
}
