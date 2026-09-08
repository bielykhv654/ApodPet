package random_photos_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import common_ui.NasaPhotoCard
import common_ui.ShowNetworkResponseNotification
import common_ui.is26Ios
import utils.NasaPhotoUiModel
import random_photos_component.integration.RandomPhotosStore

@Composable
fun RandomPhotosScreen(
    component: RandomPhotosComponent,
    onOpenDetail: (NasaPhotoUiModel) -> Unit
) {
    val state by component.model.collectAsState()

    ShowNetworkResponseNotification(
        state = state,
        isLoading = { it.loadingState is RandomPhotosStore.LoadingState.Loading },
        isError = { it.loadingState is RandomPhotosStore.LoadingState.Error },
        errorMessage = { (it.loadingState as RandomPhotosStore.LoadingState.Error).error.message },
        onClose = { component.resetLoadingState() }
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(state.photos, key = { photo -> photo.id }) { photo ->
            val isSaved = state.savedIds.contains(photo.id)
            val uiModel = photo.copy(isSaved = isSaved)

            NasaPhotoCard(
                photo = uiModel,
                onToggleSave = { component.toggleSave(it) },
                onOpenDetail = { onOpenDetail(uiModel) }
            )
        }
        if (is26Ios())
            item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
