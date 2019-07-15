package com.chaitanya.android.delivery.model.repository.databse.entity

 sealed class DeliveryResult {
    object Loading : DeliveryResult()
    data class Success(val response: Any?) : DeliveryResult()
    data class Failure(val throwable: Throwable) : DeliveryResult()



}