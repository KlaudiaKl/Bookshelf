package com.klaudia.bookshelf.presentation.screens.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klaudia.bookshelf.data.BooksRepository
import com.klaudia.bookshelf.data.RequestState
import com.klaudia.bookshelf.model.VolumeApiResponse
import com.klaudia.bookshelf.model.VolumeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: BooksRepository) : ViewModel() {
    private val _newestBooks = MutableStateFlow<RequestState<VolumeApiResponse?>>(RequestState.Loading)
    val newestBooks : StateFlow<RequestState<VolumeApiResponse?>> = _newestBooks.asStateFlow()

    private val _volumesByCategory = MutableStateFlow<RequestState<VolumeApiResponse?>>(RequestState.Loading)
    val volumesByCategory : StateFlow<RequestState<VolumeApiResponse?>> = _newestBooks.asStateFlow()

    init {
       loadBooks("science fiction")
        //loadBooksOfCategory("Science Fiction & Fantasy")
    }
    fun loadBooks(query: String){
        viewModelScope.launch {
            val result = repository.listVolumesFromNewest(query)
            if (result !=null){
                _newestBooks.value = RequestState.Success(result)
            }
            else{
                _newestBooks.value = RequestState.Error(Exception("Api call unsuccessful"))
            }

        }
    }

    fun loadBooksOfCategory(category: String){
        viewModelScope.launch {
            val result = repository.listVolumesByCategory(category)
            if (result!=null){
                _newestBooks.value = RequestState.Success(result)
            }

            else{
                _newestBooks.value = RequestState.Error(Exception("Api call unsuccessful"))
            }
        }
    }
}