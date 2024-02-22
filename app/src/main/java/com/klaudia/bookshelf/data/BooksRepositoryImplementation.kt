package com.klaudia.bookshelf.data

import javax.inject.Inject

class BooksRepositoryImplementation @Inject constructor(private val apiService: BooksApi) :
    BooksRepository {
    override suspend fun searchBooks(query: String) {
        apiService.searchBooks(query)
    }
}