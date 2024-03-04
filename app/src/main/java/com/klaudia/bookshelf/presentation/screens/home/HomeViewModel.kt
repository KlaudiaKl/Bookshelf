package com.klaudia.bookshelf.presentation.screens.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klaudia.bookshelf.data.BooksRepository
import com.klaudia.bookshelf.data.RequestState
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: BooksRepository) : ViewModel() {
    private val _newestBooks = MutableStateFlow<RequestState<VolumeApiResponse?>>(RequestState.Loading)
    val newestBooks : StateFlow<RequestState<VolumeApiResponse?>> = _newestBooks.asStateFlow()

    private val _volumesByCategory = MutableStateFlow<RequestState<VolumeApiResponse?>>(RequestState.Loading)
    val volumesByCategory : StateFlow<RequestState<VolumeApiResponse?>> = _volumesByCategory.asStateFlow()

    private val _oopBooks = MutableStateFlow<RequestState<List<VolumeItem>>>(RequestState.Loading)
    val oopBooks : StateFlow<RequestState<List<VolumeItem>>> = _oopBooks.asStateFlow()

    private val _kotlinBooks = MutableStateFlow<RequestState<List<VolumeItem>>>(RequestState.Loading)
    val kotlinBooks : StateFlow<RequestState<List<VolumeItem>>> = _kotlinBooks.asStateFlow()

    private val _composeBooks = MutableStateFlow<RequestState<List<VolumeItem>>>(RequestState.Loading)
    val composeBooks : StateFlow<RequestState<List<VolumeItem>>> = _composeBooks.asStateFlow()

    init {
       loadBooks("android")
        loadOopBooks()
        loadKotlinBooks()
        loadComposeBooks()
        //loadBooksOfCategory("Science Fiction & Fantasy")
    }
    fun loadBooks(query: String){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){ repository.listVolumesFromNewest(query)}
            if (result !=null){
                _newestBooks.value = RequestState.Success(result)
            }
            else{
                _newestBooks.value = RequestState.Error(Exception("Api call unsuccessful"))
            }
        }
    }

   fun loadOopBooks(){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){ repository.searchBooks("Object oriented Programming", 0, filter = null)}
            if (result !=null){
                when(result){
                    is RequestState.Success -> {
                        _oopBooks.value = RequestState.Success(result.data.items )
                    }
                    is RequestState. Error -> {
                        _oopBooks.value = RequestState.Error(result.exception)
                    }
                    is RequestState.Loading -> {
                        _oopBooks.value = RequestState.Loading
                    }
                }

            }
            else{
                _oopBooks.value = RequestState.Error(Exception("Api call unsuccessful"))
            }
        }
    }

    fun loadKotlinBooks(){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){ repository.searchBooks("Kotlin", 0, filter = null)}
            if (result !=null){
                when(result){
                    is RequestState.Success -> {
                        _kotlinBooks.value = RequestState.Success(result.data.items )
                    }
                    is RequestState. Error -> {
                        _kotlinBooks.value = RequestState.Error(result.exception)
                    }
                    is RequestState.Loading -> {
                        _kotlinBooks.value = RequestState.Loading
                    }
                }

            }
            else{
                _kotlinBooks.value = RequestState.Error(Exception("Api call unsuccessful"))
            }
        }
    }
    fun loadComposeBooks(){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){ repository.searchBooks("Jetpack Compose", 0, filter = null)}
            if (result !=null){
                when(result){
                    is RequestState.Success -> {
                        _composeBooks.value = RequestState.Success(result.data.items )
                    }
                    is RequestState. Error -> {
                        _composeBooks.value = RequestState.Error(result.exception)
                    }
                    is RequestState.Loading -> {
                        _composeBooks.value = RequestState.Loading
                    }
                }

            }
            else{
                _composeBooks.value = RequestState.Error(Exception("Api call unsuccessful"))
            }
        }
    }

   /* fun loadBooksOfCategory(category: String){
        viewModelScope.launch {
            val result = repository.listVolumesByCategory(category)
            if (result!=null){
                _newestBooks.value = RequestState.Success(result)
            }

            else{
                _newestBooks.value = RequestState.Error(Exception("Api call unsuccessful"))
            }
        }
    }*/

    val combinedBooksState: Flow<CombinedBooksState> = combine(
        newestBooks, oopBooks, kotlinBooks, composeBooks
    ) { newest, oop, kotlin, compose ->
        // Handle the combination of states here
        // This is a simplified example. You'll need to adjust it based on how you handle loading and errors.
        CombinedBooksState(
            newestVolumes = (newest as? RequestState.Success)?.data?.items,
            oopBooks = (oop as? RequestState.Success)?.data,
            kotlinBooks = (kotlin as? RequestState.Success)?.data,
            composeBooks = (compose as? RequestState.Success)?.data,
            isLoading = listOf(newest, oop, kotlin, compose).any { it is RequestState.Loading },
            error = listOf(newest, oop, kotlin, compose).firstOrNull { it is RequestState.Error }?.let { (it as RequestState.Error).exception }
        )
    }.catch { e ->
        emit(CombinedBooksState(error = e))
    }.onStart {
        emit(CombinedBooksState(isLoading = true))
    }

}


data class CombinedBooksState(
    val newestVolumes: List<VolumeItem>? = null,
    val oopBooks: List<VolumeItem>? = null,
    val kotlinBooks: List<VolumeItem>? = null,
    val composeBooks: List<VolumeItem>? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)