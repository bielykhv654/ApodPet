package photo_details_component

import com.arkivanov.decompose.ComponentContext
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDetails(
    val title: String,
    val explanation: String,
    val imageUrl: String,
    val date: String,
    val copyright: String? = null,
    val mediaType: String? = null
)

interface PhotoDetailsComponent {
    val photoDetails: PhotoDetails

    fun onBack()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            photoDetails: PhotoDetails,
            onBack: () -> Unit
        ): PhotoDetailsComponent
    }
}
