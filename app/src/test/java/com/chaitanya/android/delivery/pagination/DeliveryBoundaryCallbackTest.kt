package com.chaitanya.android.delivery.pagination

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.chaitanya.android.delivery.BuildConfig
import com.chaitanya.android.delivery.R.string.error
import com.chaitanya.android.delivery.model.repository.IDeliveryRepository
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.chaitanya.android.delivery.util.RxJavaTestHooksResetRule
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import java.net.ConnectException


@RunWith(MockitoJUnitRunner::class)
class DeliveryBoundaryCallbackTest{

    @get:Rule
    var rxJavaTestHooksResetRule = RxJavaTestHooksResetRule()
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var deliveryRepository: IDeliveryRepository

    private lateinit var deliveryBoundaryCallback: DeliveryBoundaryCallback
    private lateinit var  spy:DeliveryBoundaryCallback

    @Spy
    var mockDeliveryList = listOf<Delivery>()

    @Before
    fun setUp() {
        deliveryRepository = Mockito.mock(IDeliveryRepository::class.java)
        deliveryBoundaryCallback = DeliveryBoundaryCallback(deliveryRepository, CompositeDisposable())
        spy = spy(deliveryBoundaryCallback)
    }

    @Test
    fun initLoadTest() {
        given(deliveryRepository.getDeliveries(any(), any())).willReturn(Single.just(mock()))
        spy.onZeroItemsLoaded()
        verify(spy, times(1)).fetchNetwork(0, BuildConfig.PAGE_SIZE)
        assert(spy.loading.value==true)

    }

    @Test
    fun endLoadTest() {
        given(deliveryRepository.getDeliveries(any(), any())).willReturn(Single.just(mock()))
        spy.onItemAtEndLoaded(mock())
        verify(spy, times(1)).fetchNetwork(any(), any())
        assert(spy.loading.value==true)
    }

    @Test
    fun retryTest() {
        given(deliveryRepository.getDeliveries(any(), any())).willReturn(Single.just(mock()))
        spy.retry()
        verify(spy, times(1)).fetchNetwork(any(), any())
        assert(spy.loading.value==true)
    }

    @Test
    fun checkAllLoaded() {

        Mockito.`when`(mockDeliveryList.size).thenReturn(10)
        spy.success(mockDeliveryList)
        assert(spy.isLoaded)

    }

    @Test
    fun success() {
        Mockito.`when`(mockDeliveryList.size).thenReturn(20)
        spy.success(mockDeliveryList)
        assert(spy.success.value ==true)
    }

    @Test
    fun errorConnectionTest() {
        deliveryBoundaryCallback.error( ConnectException())
        assert(spy.error.value== com.chaitanya.android.delivery.R.string.network_error)
    }

    @Test
    fun errorTest() {
        deliveryBoundaryCallback.error( Exception())
        assert(spy.error.value== error)

    }

    @Test
    fun refreshTest() {
        given(deliveryRepository.getDeliveries(any(), any())).willReturn(Single.just(mock()))
        spy.onRefresh()
        assert(spy.totalCount == 0)
        verify(spy, times(1)).onZeroItemsLoaded()
        verify(spy, times(1)).fetchNetwork(0, BuildConfig.PAGE_SIZE)
        assert(spy.loading.value==true)
    }


}