package com.klaudia.bookshelf.model

data class SaleInfo(
    val country: String,
    val saleability: String,
    val isEbook: Boolean,
    val listPrice: Price?,
    val retailPrice: Price?,
    val buyLink: String?,
    val offers: List<Offer>?
)
