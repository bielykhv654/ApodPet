package date_range_component.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import utils.NasaPhotoUiModel
import utils.StringMessage

class NasaPagingException(error: StringMessage) : Exception(error.toString())

typealias NasaPageLoader = suspend (cursorDate: String?, pageSizeDays: Int) -> NextPagePhotosResult

class NasaPagingSource(
    private val loader: NasaPageLoader
) : PagingSource<String, NasaPhotoUiModel>() {

    override fun getRefreshKey(state: PagingState<String, NasaPhotoUiModel>): String? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.nextKey ?: page.prevKey
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, NasaPhotoUiModel> {
        val currentMinDate = params.key
        val pageSizeDays = params.loadSize.coerceAtMost(20)

        return when (val res = loader.invoke(currentMinDate, pageSizeDays)) {
            is NextPagePhotosResult.Success -> {
                LoadResult.Page(
                    data = res.photos,
                    prevKey = null,
                    nextKey = res.nextMinDate
                )
            }
            is NextPagePhotosResult.Error -> {
                LoadResult.Error(NasaPagingException(res.error))
            }
        }
    }
}
