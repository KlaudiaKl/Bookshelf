package com.klaudia.bookshelf.data

import android.util.Log
import com.klaudia.bookshelf.db.SavedVolume
import com.klaudia.bookshelf.db.VolumeDao
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class BooksRepositoryImplementation @Inject constructor(private val apiService: BooksApi, private val volumeDao: VolumeDao) :
    BooksRepository {
    override suspend fun searchBooks(query: String, startIndex: Int, filter: String?): RequestState<VolumeApiResponse>? {
        return try {
            val response = apiService.searchBooks(query, startIndex, filter = filter)
            if(response.isSuccessful && response.body()!=null){
                RequestState.Success(response.body()!!)
            }
            else{
                Log.d("Error", "response unsuccessful")
                RequestState.Error(Exception("Response unsuccessful"))
            }

        } catch (e:Exception){
            e.message?.let { Log.d("Error", it) }
            RequestState.Error(e)
        }
    }

    override suspend fun getVolumeById(id: String): RequestState<VolumeItem> {
        return try {
            val response = apiService.getVolumeById(id)
            if (response.isSuccessful&&response.body()!=null){
                RequestState.Success(response.body()!!)
            }
            else{
                Log.d("Error", "Response unsuccessful")
                RequestState.Error(Exception("Response unsuccessful"))
            }
        }
        catch (e:Exception){
            e.message?.let { Log.d("Error", it) }
            RequestState.Error(e)
        }
    }


    override suspend fun listVolumesFromNewest(query: String): VolumeApiResponse? {
        return try {
            val response = apiService.listVolumesFromNewest(query)
            if(response.isSuccessful){
                response.body()
            }
            else{
                Log.d("Error", "response unsuccessful")
                null
            }

        } catch (e:Exception){
            e.message?.let { Log.d("Error", it) }
            null
        }
    }

    override suspend fun listVolumesByCategory(category: String): VolumeApiResponse? {
        return try {
            val response = apiService.listVolumesByMainCategory(category)
            if(response.isSuccessful){
                response.body()
            }
            else{
                Log.d("Error", "response unsuccessful")
                null
            }

        } catch (e:Exception){
            e.message?.let { Log.d("Error", it) }
            null
        }
    }

    override suspend fun insertVolumeToSaved(volume: SavedVolume) {
        volumeDao.insert(volume)
    }

    override fun getAllSavedVolumes(): Flow<List<SavedVolume>> {
        return volumeDao.getAllSavedVolumes()
    }

    override suspend fun deleteVolumeFromSaved(volumeId: String) {
        return volumeDao.deleteById(volumeId)
    }

    override fun isVolumeSaved(volumeId: String): Flow<Boolean> = volumeDao.isVolumeSaved(volumeId)

}