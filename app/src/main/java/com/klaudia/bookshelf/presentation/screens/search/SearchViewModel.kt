package com.klaudia.bookshelf.presentation.screens.search


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klaudia.bookshelf.data.BooksRepository
import com.klaudia.bookshelf.data.RequestState
import com.klaudia.bookshelf.model.VolumeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val booksRepository: BooksRepository) :
    ViewModel() {
    private val _searchResults =
        MutableStateFlow<RequestState<List<VolumeItem>>>(RequestState.Loading)
    val searchResults: StateFlow<RequestState<List<VolumeItem>>> = _searchResults.asStateFlow()
    private var items = mutableListOf<VolumeItem>()
    private var startIndex = 0


    private var searchJob: Job? = null
    fun search(query: String, loadMore: Boolean, filter: String?) {
       // Log.d("LoadMore", loadMore.toString())
        if (loadMore) {
            startIndex += 10
        } else {
            startIndex = 0
            items.clear()
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                booksRepository.searchBooks(
                    query,
                    startIndex = startIndex,
                    filter = filter
                )
            }
            when (result) {
                is RequestState.Success -> {
                    if (result.data != null) {
                        val newItems = result.data.items.filterNot { newItem -> items.any { existingItem -> existingItem.id == newItem.id } }
                        items.addAll(newItems)
                       // Log.d("items", items.size.toString())
                        _searchResults.value = RequestState.Success(items.toList())
                    } else {
                        _searchResults.value =
                            RequestState.Error(Exception("Api call unsuccessful"))
                    }
                }
                is RequestState.Error -> {
                    _searchResults.value = RequestState.Error(Exception("Api call unsuccessful"))
                }
                else -> {
                    _searchResults.value = RequestState.Loading
                }
            }

        }

    }
}