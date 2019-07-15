package com.chaitanya.android.delivery.model.repository.network

import com.chaitanya.android.delivery.model.repository.network.response.DeliveryItemResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query



/**
 * get delivery list
 * Trigger a request to Delivery API with the following params:
 * @param offset starting index
 * @param limit items per request

 */





/**
 * get delivery list  API communication setup via Retrofit.
 */

interface DeliveryAPI {

    /**
     * Get delivery list
     */
    @GET("deliveries/")
    fun getDeliveryList(@Query("offset") offset: Int, @Query("limit") limit: Int): Single<List<DeliveryItemResponse>>

}