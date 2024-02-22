package com.klaudia.bookshelf.data

import com.klaudia.bookshelf.BuildConfig
import com.klaudia.bookshelf.model.VolumeApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApi {

    @GET("books/v1/volumes")
    suspend fun searchBooks(@Query("q") query: String, @Query("key") apiKey: String = BuildConfig.API_KEY) : Response<VolumeApiResponse>

}