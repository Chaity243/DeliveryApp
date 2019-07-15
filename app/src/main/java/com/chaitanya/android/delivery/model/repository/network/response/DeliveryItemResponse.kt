package com.chaitanya.android.delivery.model.repository.network.response

data class DeliveryItemResponse(
    val id: Int,
    val description: String,
    val imageUrl: String,
    val location: ItemLocationResponse
)