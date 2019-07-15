@file:Suppress("KotlinDeprecation")

package com.chaitanya.android.delivery.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chaitanya.android.delivery.model.repository.IDeliveryRepository
import javax.inject.Inject

/**
 * Factory for ViewModels
 */

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val deliveryRepository: IDeliveryRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DeliveryViewModel::class.java)) {
            DeliveryViewModel(deliveryRepository) as T

        }
        else {
            throw IllegalArgumentException("ViewModel Not Found") as Throwable
        }
    }

}