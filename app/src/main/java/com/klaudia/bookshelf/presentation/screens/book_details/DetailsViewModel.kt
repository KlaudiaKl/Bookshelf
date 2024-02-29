package com.klaudia.bookshelf.presentation.screens.book_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klaudia.bookshelf.data.BooksRepository
import com.klaudia.bookshelf.data.RequestState
import com.klaudia.bookshelf.model.VolumeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BooksRepository) : ViewModel() {

    private val _volumeItem = MutableStateFlow<RequestState<VolumeItem>>(RequestState.Loading)
    val volumeItem: StateFlow<RequestState<VolumeItem>> = _volumeItem.asStateFlow()

    fun getVolume(id: String){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){ repository.getVolumeById(id)}
            if (result !=null){
                _volumeItem.value = result
            }
            else{
                _volumeItem.value = RequestState.Error(Exception("Api call unsuccessful"))
            }
        }
    }
}