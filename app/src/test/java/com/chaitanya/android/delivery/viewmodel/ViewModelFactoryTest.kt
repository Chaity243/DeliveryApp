package com.chaitanya.android.delivery.viewmodel

import android.arch.lifecycle.ViewModel
import com.chaitanya.android.delivery.model.repository.IDeliveryRepository
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ViewModelFactoryTest {

    @Mock
    lateinit var deliveryRepository: IDeliveryRepository

    lateinit var viewModelFactory: ViewModelFactory

    @Before
    fun setUp() {
        viewModelFactory = ViewModelFactory(deliveryRepository)
    }

    @Test
    fun deliveryViewModelTest() {
        given(deliveryRepository.getDeliveries()).willReturn(mock())
        val model: Any = viewModelFactory.create(DeliveryViewModel::class.java)
        assert(model is DeliveryViewModel)
    }



    @Test(expected = IllegalArgumentException::class)
    fun unkownViewModelTest() {
        val model: Any = viewModelFactory.create(ViewModel::class.java)
        assert(model is DeliveryViewModel)
    }
}