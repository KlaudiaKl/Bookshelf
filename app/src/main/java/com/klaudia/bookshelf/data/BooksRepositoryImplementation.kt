package com.klaudia.bookshelf.data

import android.util.Log
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class BooksRepositoryImplementation @Inject constructor(private val apiService: BooksApi) :
    BooksRepository {
    override suspend fun searchBooks(query: String): VolumeApiResponse? {
        return try {
            val response = apiService.searchBooks(query)
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
}