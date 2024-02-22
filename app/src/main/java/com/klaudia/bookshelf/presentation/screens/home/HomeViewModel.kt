package com.klaudia.bookshelf.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.klaudia.bookshelf.data.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: BooksRepository) : ViewModel() {
}