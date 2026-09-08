package common_ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.ui.video.VideoPlayerComposable
import chaintech.videoplayer.ui.youtube.YouTubePlayerComposable
import coil3.compose.AsyncImage

@Composable
fun NasaMediaView(
    imageUrl: String,
    title: String,
    mediaType: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val isVideo = mediaType.equals("video", ignoreCase = true) ||
            imageUrl.contains("youtube.com") ||
            imageUrl.contains("youtu.be") ||
            imageUrl.endsWith(".mp4") ||
            imageUrl.endsWith(".m3u8")

    if (isVideo) {
        val isYouTube = imageUrl.contains("youtube.com") || imageUrl.contains("youtu.be")
        val playerHost = remember(imageUrl) {
            MediaPlayerHost(
                mediaUrl = imageUrl,
                autoPlay = false,
                isLooping = false
            )
        }

        if (isYouTube) {
            YouTubePlayerComposable(
                modifier = modifier,
                playerHost = playerHost
            )
        } else {
            VideoPlayerComposable(
                modifier = modifier,
                playerHost = playerHost
            )
        }
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = title,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}
