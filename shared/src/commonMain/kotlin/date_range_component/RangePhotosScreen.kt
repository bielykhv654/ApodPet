package date_range_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import apodpet.shared.generated.resources.Res
import apodpet.shared.generated.resources.btn_retry
import apodpet.shared.generated.resources.msg_no_photos
import common_ui.LoadingAnimation
import common_ui.NasaPhotoCard
import common_ui.ShowNetworkResponseNotification
import common_ui.is26Ios
import org.jetbrains.compose.resources.stringResource
import date_range_component.integration.RangePhotosStore
import utils.NasaPhotoUiModel

@Composable
fun RangePhotosScreen(
    component: RangePhotosComponent,
    onOpenDetail: (NasaPhotoUiModel) -> Unit
) {
    val model by component.model.collectAsState()
    val lazyPagingItems = model.pagingDataFlow.collectAsLazyPagingItems()
    val refreshState = lazyPagingItems.loadState.refresh

    ShowNetworkResponseNotification(
        state = model,
        isLoading = {
            it.loadingState is RangePhotosStore.LoadingState.Loading ||
                    refreshState is LoadState.Loading
        },
        isError = { it.loadingState is RangePhotosStore.LoadingState.Error },
        errorMessage = { (it.loadingState as RangePhotosStore.LoadingState.Error).error.message },
        onClose = { component.resetLoadingState()}
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (lazyPagingItems.itemCount == 0 && refreshState !is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(Res.string.msg_no_photos), color = MaterialTheme.colorScheme.onSurface)
                }
            }
        } else {
            items(
                count = lazyPagingItems.itemCount,
                key = { index ->
                    val photo = lazyPagingItems.peek(index)
                    photo?.id ?: index
                }
            ) { index ->
                val photo: NasaPhotoUiModel? = lazyPagingItems[index]
                if (photo != null) {
                    val isSaved = model.savedIds.contains(photo.id)
                    val uiModel = photo.copy(isSaved = isSaved)

                    NasaPhotoCard(
                        photo = uiModel,
                        onToggleSave = { component.toggleSave(it) },
                        onOpenDetail = { onOpenDetail(uiModel) }
                    )
                }
            }

            when (lazyPagingItems.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingAnimation()
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(onClick = { lazyPagingItems.retry() }) {
                                Text(stringResource(Res.string.btn_retry))
                            }
                        }
                    }
                }

                else -> Unit
            }
        }
        if (is26Ios())
            item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}
