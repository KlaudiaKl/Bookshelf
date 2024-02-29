package com.klaudia.bookshelf.data

import com.klaudia.bookshelf.BuildConfig
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApi {

    @GET("books/v1/volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        //@Query("maxResults") maxResults: Int = 20,
        @Query("startIndex") startIndex: Int = 0,
        @Query("key") apiKey: String = BuildConfig.API_KEY) : Response<VolumeApiResponse>

    @GET("books/v1/volumes/{volumeId}")
    suspend fun getVolumeById(
        @Path("volumeId") id: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY) : Response<VolumeItem>


    @GET("books/v1/volumes")
    suspend fun listVolumesFromNewest(
        @Query("q") query: String,
        @Query("orderBy") orderBy: String = "newest",
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<VolumeApiResponse>

    @GET("books/v1/volumes")
    suspend fun listVolumesByMainCategory(
        @Query("mainCategory") category: String,
        @Query("key") apiKey : String = BuildConfig.API_KEY
    ) : Response<VolumeApiResponse>
}