package com.klaudia.bookshelf.presentation.screens.saved_volumes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klaudia.bookshelf.data.BooksRepository
import com.klaudia.bookshelf.data.RequestState
import com.klaudia.bookshelf.db.SavedVolume
import com.klaudia.bookshelf.model.VolumeApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SavedVolumesViewModel @Inject constructor(private val repository: BooksRepository): ViewModel() {
    private val _volumes = MutableStateFlow<RequestState<List<SavedVolume>>> (RequestState.Loading)
    val volumes : StateFlow<RequestState<List<SavedVolume>>> = _volumes.asStateFlow()

    init {
        getSavedBooks()
    }

    fun getSavedBooks(){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){ repository.getAllSavedVolumes()}
            if (result !=null){
                _volumes.value = RequestState.Success(result)
            }
            else{
                _volumes.value = RequestState.Error(Exception("Api call unsuccessful"))
            }
        }
    }

}