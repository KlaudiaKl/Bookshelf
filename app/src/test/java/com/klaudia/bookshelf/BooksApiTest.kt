package com.klaudia.bookshelf

import com.klaudia.bookshelf.data.BooksApi
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import junit.framework.Assert.fail
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.net.HttpURLConnection

class BooksApiTest {
    private lateinit var api: BooksApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp(){
      mockWebServer = MockWebServer()
        api = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }

    @Test
    fun testApiCall() = runBlocking {
        val mockVolumeJson = """
        {
          "id": "testVolumeId",
          "volumeInfo": {
            "title": "Test Book Title",
            "authors": ["Author One", "Author Two"],
            "publisher": "Test Publisher",
            "publishedDate": "2022-01-01",
            "description": "Test Description",
            "pageCount": 123,
            "imageLinks": {
              "thumbnail": "http://example.com/testthumbnail.jpg"
            },
            "language": "en",
            "previewLink": "http://example.com/previewlink",
            "infoLink": "http://example.com/infolink"
          }
        }
    """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(mockVolumeJson).setResponseCode(HttpURLConnection.HTTP_OK))

        val response = api.getVolumeById("testVolumeId", BuildConfig.API_KEY)
        assertTrue(response.isSuccessful)
        response.body()?.let {
            item ->
            assertEquals("testVolumeId", item.id)
            assertEquals("Test Book Title", item.volumeInfo.title)
            assertEquals("en", item.volumeInfo.language)
        }?: fail("VolumeItem was null")
        val request = mockWebServer.takeRequest()
        assertEquals("/books/v1/volumes/testVolumeId?key=${BuildConfig.API_KEY}", request.path)
    }

    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}
