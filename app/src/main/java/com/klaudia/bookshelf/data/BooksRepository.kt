package com.klaudia.bookshelf.data

interface BooksRepository {
    suspend fun searchBooks(query:String)
}