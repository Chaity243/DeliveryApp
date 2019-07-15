package com.chaitanya.android.delivery.model.repository

import com.chaitanya.android.delivery.model.mapper.DeliveryMapper
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.chaitanya.android.delivery.model.repository.network.DeliveryAPI
import com.chaitanya.android.delivery.pagination.DeliveryDataSource
import io.reactivex.Single

/**
 * DeliveryRepository implements IDeliveryRepository
 *
 * DeliveryRepository fetches the data info from the api layer
 */
class DeliveryRepository(
    private val  deliveryAPI: DeliveryAPI,
    private val deliveryMapper: DeliveryMapper,
    private val deliveryDataSource: DeliveryDataSource
) : IDeliveryRepository {

    override fun getDeliveries(offset: Int, limit: Int): Single<List<Delivery>> {
        return deliveryAPI.getDeliveryList(offset, limit)
            .map {

                val list: List<Delivery> = deliveryMapper.map(it)
                if (offset == 0) {
                    deliveryDataSource.clearAll()
                }
                deliveryDataSource.insertIntoLocal(list)
                return@map list
            }
    }

    override fun getDeliveries() = deliveryDataSource.getAllDeliveries()

    override fun addToLocal(deliveries: List<Delivery>) = deliveryDataSource.insertIntoLocal(deliveries)

    override fun getDelivery(id: Int) = deliveryDataSource.getDeliveryAgainstId(id)

    override fun getCount() = deliveryDataSource.getDeliveryItemsCount()

    override fun removeAll() = deliveryDataSource.clearAll()
}