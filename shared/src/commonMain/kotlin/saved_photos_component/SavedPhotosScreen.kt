package saved_photos_component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import apodpet.shared.generated.resources.Res
import apodpet.shared.generated.resources.msg_no_saved_photos
import common_ui.NasaPhotoCard
import common_ui.is26Ios
import org.jetbrains.compose.resources.stringResource
import utils.NasaPhotoUiModel

@Composable
fun SavedPhotosScreen(
    component: SavedPhotosComponent,
    onOpenDetail: (NasaPhotoUiModel) -> Unit
) {
    val model by component.model.collectAsState()

    if (model.savedPhotos.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.msg_no_saved_photos),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(model.savedPhotos, key = { it.id }) { photo ->
                NasaPhotoCard(
                    photo = photo,
                    onDelete = { component.toggleSavePhoto(it) },
                    onOpenDetail = { onOpenDetail(photo) }
                )
            }
            if (is26Ios())
                item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}
