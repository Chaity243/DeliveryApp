package com.chaitanya.android.delivery.model.mapper

import com.chaitanya.android.delivery.model.repository.network.response.DeliveryItemResponse
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.chaitanya.android.delivery.model.repository.databse.entity.Location
import javax.inject.Inject

class DeliveryMapper @Inject constructor() {

    fun map(responseList: List<DeliveryItemResponse>): List<Delivery> {
        return responseList.map { (map(it)) }
    }

    private fun map(response: DeliveryItemResponse): Delivery {
        return Delivery(
            id = response.id,
            imageUrl = response.imageUrl,
            description = response.description,
            location = Location(
                response.location.lat,
                response.location.lng,
                response.location.address
            )
        )
    }
}