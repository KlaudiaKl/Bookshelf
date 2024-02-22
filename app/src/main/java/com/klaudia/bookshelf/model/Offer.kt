package com.klaudia.bookshelf.model

data class Offer(
    val finskyOfferType: Int,
    val listPrice: Price,
    val retailPrice: Price
)
