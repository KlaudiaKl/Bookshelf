package com.klaudia.bookshelf

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.klaudia.bookshelf.data.BooksRepository
import com.klaudia.bookshelf.data.RequestState
import com.klaudia.bookshelf.model.AccessInfo
import com.klaudia.bookshelf.model.FormatAvailability
import com.klaudia.bookshelf.model.ImageLinks
import com.klaudia.bookshelf.model.IndustryIdentifier
import com.klaudia.bookshelf.model.Offer
import com.klaudia.bookshelf.model.PanelizationSummary
import com.klaudia.bookshelf.model.Price
import com.klaudia.bookshelf.model.ReadingModes
import com.klaudia.bookshelf.model.SaleInfo
import com.klaudia.bookshelf.model.SearchInfo
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeInfo
import com.klaudia.bookshelf.model.VolumeItem
import com.klaudia.bookshelf.presentation.screens.search.SearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var booksRepo: BooksRepository

    @Before
    fun setUp(){
        booksRepo = mockk()
        viewModel = SearchViewModel(booksRepo)

    }

    @Test
    fun `search books returns success state with expected data`() = runTest {
        val query = "Kotlin Programming"
        val dummyIndustryIdentifier = IndustryIdentifier("ISBN_10", "1234567890")
        val dummyReadingModes = ReadingModes(true, false)
        val dummyPanelizationSummary = PanelizationSummary(false, false)
        val dummyImageLinks = ImageLinks("http://smallthumbnail.com", "http://thumbnail.com")
        val dummyEpub = FormatAvailability(true, "http://epubdownloadlink.com")
        val dummyPdf = FormatAvailability(false, null)
        val dummyListPrice = Price(19.99, "USD")
        val dummyRetailPrice = Price(15.99, "USD")
        val dummyOffer = Offer(1, dummyListPrice, dummyRetailPrice)

        val dummySaleInfo = SaleInfo(
            country = "US",
            saleability = "FOR_SALE",
            isEbook = true,
            listPrice = dummyListPrice,
            retailPrice = dummyRetailPrice,
            buyLink = "http://buylink.com",
            offers = listOf(dummyOffer)
        )
        val dummyAccessInfo = AccessInfo(
            country = "US",
            viewability = "PARTIAL",
            embeddable = true,
            publicDomain = false,
            textToSpeechPermission = "ALLOWED",
            epub = dummyEpub,
            pdf = dummyPdf,
            webReaderLink = "http://webreaderlink.com",
            accessViewStatus = "SAMPLE",
            quoteSharingAllowed = true
        )
        val dummyVolumeInfo = VolumeInfo(
            title = "Kotlin for Beginners",
            subtitle = "A Comprehensive Guide",
            authors = listOf("Jane Doe", "John Doe"),
            publisher = "Tech Publisher",
            publishedDate = "2022-01-01",
            description = "A comprehensive guide to Kotlin programming.",
            industryIdentifiers = listOf(dummyIndustryIdentifier),
            readingModes = dummyReadingModes,
            pageCount = 300,
            printType = "BOOK",
            categories = listOf("Programming", "Technology"),
            maturityRating = "NOT_MATURE",
            allowAnonLogging = true,
            contentVersion = "1.2.3",
            panelizationSummary = dummyPanelizationSummary,
            imageLinks = dummyImageLinks,
            language = "en",
            previewLink = "http://previewlink.com",
            infoLink = "http://infolink.com",
            canonicalVolumeLink = "http://canonicalvolumelink.com"
        )
        val dummyVolumeItems = listOf(
            VolumeItem("book", "id123", "etag123", "selfLink123", dummyVolumeInfo, dummySaleInfo, dummyAccessInfo, SearchInfo("snippet"))
        )
        val dummyVolumeApiResponse = VolumeApiResponse(
            kind = "books#volumes",
            totalItems = dummyVolumeItems.size,
            items = dummyVolumeItems
        )
        coEvery {
            booksRepo.searchBooks(query, 0, null)
        } returns RequestState.Success(dummyVolumeApiResponse)

        viewModel.search(query, false, null)
        advanceUntilIdle()
        val searchResults = viewModel.searchResults.value
        assertTrue(searchResults is RequestState.Success)
        assertEquals(dummyVolumeItems.size, (searchResults as RequestState.Success).data.size)
    }
}