package date_range_component.data

import androidx.paging.PagingSource
import database.NasaPhotoDao
import database.NasaPhotoEntity
import database.currentTimeMillis
import database.getTodayDateString
import database.minusDays
import utils.NasaPhotoUiModel
import utils.toEntity
import utils.toUiModel
import kotlinx.coroutines.flow.Flow
import network.AppClient
import network.client_requests.getNasaPhotosByRange
import network.util.Result
import utils.StringMessage
import utils.getErrorType

const val APOD_START_DATE = "1995-06-16"

sealed interface NextPagePhotosResult {
    data class Success(val photos: List<NasaPhotoUiModel>, val nextMinDate: String?) : NextPagePhotosResult
    data class Error(val error: StringMessage) : NextPagePhotosResult
}

interface RangePhotosRepository {
    fun getPhotosPagingSource(onError: ((StringMessage) -> Unit)? = null): PagingSource<String, NasaPhotoUiModel>
    suspend fun getNextPhotoPage(currentMinDate: String?, pageSizeDays: Int = 20): NextPagePhotosResult
    fun observeSavedPhotos(): Flow<List<NasaPhotoEntity>>
    suspend fun savePhoto(photo: NasaPhotoUiModel)
    suspend fun deletePhoto(id: String)
}

class RangePhotosRepositoryImpl(
    private val appClient: AppClient,
    private val photoDao: NasaPhotoDao
) : RangePhotosRepository {

    override fun getPhotosPagingSource(onError: ((StringMessage) -> Unit)?): PagingSource<String, NasaPhotoUiModel> {
        val loader: NasaPageLoader = { cursorDate, pageSizeDays ->
            val result = getNextPhotoPage(cursorDate, pageSizeDays)
            if (result is NextPagePhotosResult.Error) {
                onError?.invoke(result.error)
            }
            result
        }
        return NasaPagingSource(loader)
    }

    override suspend fun getNextPhotoPage(
        currentMinDate: String?,
        pageSizeDays: Int
    ): NextPagePhotosResult {
        val today = getTodayDateString()
        var endDate = currentMinDate ?: today

        if (endDate > today) {
            endDate = today
        }

        if (endDate < APOD_START_DATE) {
            return NextPagePhotosResult.Success(emptyList(), null)
        }

        var startDate = minusDays(endDate, pageSizeDays)
        if (startDate < APOD_START_DATE) {
            startDate = APOD_START_DATE
        }
        if (startDate > endDate) {
            startDate = endDate
        }

        return when (val res = appClient.getNasaPhotosByRange(startDate, endDate)) {
            is Result.Success -> {
                val filtered = res.data.reversed()
                    .filter { it.url != null }
                    .map { dto -> dto.toUiModel() }
                val nextMinDate = if (startDate > APOD_START_DATE) {
                    minusDays(startDate, 1)
                } else {
                    null
                }
                NextPagePhotosResult.Success(filtered, nextMinDate)
            }
            is Result.Error -> {
                NextPagePhotosResult.Error(getErrorType(res.error))
            }
        }
    }

    override fun observeSavedPhotos(): Flow<List<NasaPhotoEntity>> {
        return photoDao.observeSavedPhotos()
    }

    override suspend fun savePhoto(photo: NasaPhotoUiModel) {
        photoDao.insertPhoto(photo.toEntity(currentTimeMillis()))
    }

    override suspend fun deletePhoto(id: String) {
        photoDao.deletePhoto(id)
    }
}
