package com.klaudia.bookshelf.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klaudia.bookshelf.data.BooksRepository
import com.klaudia.bookshelf.data.RequestState
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SearchViewModel @Inject constructor(private val booksRepository: BooksRepository): ViewModel() {
    private val _searchResults = MutableStateFlow<RequestState<VolumeApiResponse?>>(RequestState.Loading)
    val searchResults : StateFlow<RequestState<VolumeApiResponse?>> = _searchResults.asStateFlow()
    var items = emptyList<VolumeItem>()

    private var searchJob: Job? = null
    fun search(query: String){
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            val result = booksRepository.searchBooks(query)
            if (result!=null){
                _searchResults.value = RequestState.Success(result)
            }
            else{
                _searchResults.value = RequestState.Error(Exception("Api call unsuccessful"))
            }

        }

    }
}