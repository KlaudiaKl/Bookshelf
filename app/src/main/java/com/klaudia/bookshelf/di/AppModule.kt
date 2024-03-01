package com.klaudia.bookshelf.di

import android.content.Context
import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.klaudia.bookshelf.data.BooksApi
import com.klaudia.bookshelf.data.BooksRepository
import com.klaudia.bookshelf.data.BooksRepositoryImplementation
import com.klaudia.bookshelf.db.SavedVolumeDatabase
import com.klaudia.bookshelf.db.VolumeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideBooksApi(retrofit: Retrofit): BooksApi =
        retrofit.create(BooksApi::class.java)

    @Provides
    fun provideBooksRepository(apiService: BooksApi, volumeDao: VolumeDao): BooksRepository = BooksRepositoryImplementation(apiService, volumeDao)

    @Provides
    @Singleton
    fun provideSavedVolumeDatabase(@ApplicationContext appContext: Context): SavedVolumeDatabase{
        return Room.databaseBuilder(
            appContext,
            SavedVolumeDatabase::class.java,
            "saved_volume_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideSavedVolumeDao(savedVolumeDatabase: SavedVolumeDatabase): VolumeDao {
        return savedVolumeDatabase.volumeDao()
    }
}