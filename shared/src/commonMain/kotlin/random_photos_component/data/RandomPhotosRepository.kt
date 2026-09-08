package random_photos_component.data

import database.NasaPhotoDao
import database.currentTimeMillis
import utils.NasaPhotoUiModel
import utils.toEntity
import utils.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import network.AppClient
import network.client_requests.getNasaRandomPhotos
import network.util.Result
import utils.StringMessage
import utils.getErrorType

sealed interface RandomPhotosResult {
    data class Success(val photoList: List<NasaPhotoUiModel>) : RandomPhotosResult
    data class Error(val error: StringMessage) : RandomPhotosResult
}

interface RandomPhotosRepository {
    suspend fun getRandomPhotos(count: Int = 15): RandomPhotosResult
    fun observeSavedPhotos(): Flow<List<NasaPhotoUiModel>>
    suspend fun savePhoto(photo: NasaPhotoUiModel)
    suspend fun deletePhoto(id: String)
}

class RandomPhotosRepositoryImpl(
    private val appClient: AppClient,
    private val photoDao: NasaPhotoDao
) : RandomPhotosRepository {

    override suspend fun getRandomPhotos(count: Int): RandomPhotosResult {
        return when (val res = appClient.getNasaRandomPhotos(count)) {
            is Result.Success -> {
                val uiModels = res.data.map { dto -> dto.toUiModel() }
                RandomPhotosResult.Success(uiModels)
            }
            is Result.Error -> RandomPhotosResult.Error(getErrorType(res.error))
        }
    }

    override fun observeSavedPhotos(): Flow<List<NasaPhotoUiModel>> {
        return photoDao.observeSavedPhotos().map { entities ->
            entities.map { entity -> entity.toUiModel() }
        }
    }

    override suspend fun savePhoto(photo: NasaPhotoUiModel) {
        photoDao.insertPhoto(photo.toEntity(currentTimeMillis()))
    }

    override suspend fun deletePhoto(id: String) {
        photoDao.deletePhoto(id)
    }
}
