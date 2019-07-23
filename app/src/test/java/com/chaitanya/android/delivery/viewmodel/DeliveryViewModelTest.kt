package com.chaitanya.android.delivery.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.chaitanya.android.delivery.R
import com.chaitanya.android.delivery.model.repository.DeliveryRepository
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.chaitanya.android.delivery.model.repository.databse.entity.Location
import com.chaitanya.android.delivery.pagination.DeliveryBoundaryCallback
import com.chaitanya.android.delivery.util.AlertMessage
import com.chaitanya.android.delivery.util.RxJavaTestHooksResetRule
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.net.ConnectException


@RunWith(MockitoJUnitRunner::class)
class DeliveryViewModelTest {
    @get:Rule
    var rxJavaTestHooksResetRule = RxJavaTestHooksResetRule()
    
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()




    private lateinit var deliveryViewModel: DeliveryViewModel


    @Mock
    lateinit   var deliveryList: LiveData<PagedList<Delivery>>

    @Mock
    lateinit var deliveryRepository: DeliveryRepository
    @Mock
    lateinit var deliveryBoundaryCallback :DeliveryBoundaryCallback
    @Mock
    lateinit var config :PagedList.Config



    @Before
    fun setUp() {
        deliveryBoundaryCallback = DeliveryBoundaryCallback(deliveryRepository, CompositeDisposable())
        given(deliveryRepository.getDeliveries()).willReturn(mock())
        deliveryViewModel = DeliveryViewModel(deliveryRepository)
        deliveryViewModel.deliveryBoundaryCallback = deliveryBoundaryCallback
        deliveryList = LivePagedListBuilder(deliveryRepository.getDeliveries(), config)
            .setBoundaryCallback(deliveryBoundaryCallback).build()
        deliveryViewModel= DeliveryViewModel(deliveryRepository)


    }

    @Test
    fun deliveryListLocalTest() {
        deliveryViewModel =
            DeliveryViewModel(deliveryRepository)
        deliveryViewModel.deliveryBoundaryCallback.loading.value?.let { assert(it) }

        verify(deliveryRepository, never()).getDeliveries(any(), any())

        assert(deliveryViewModel.deliveryList.value?.size != 0)
        deliveryViewModel.deliveryBoundaryCallback.loaded.value?.let { assert(it == AlertMessage.LOADED.message) }
    }


    @Test(expected = IllegalStateException::class)
    fun onApiFailTest() {
        given(deliveryRepository.getDeliveries(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).willThrow(
            IllegalStateException()
        )
        deliveryBoundaryCallback.onZeroItemsLoaded()
        assert(deliveryBoundaryCallback.error.value== R.string.error)
    }

    @Test
    fun onApiErrorTest() {
        given(deliveryRepository.getDeliveries(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).willReturn(
            Single.error(Throwable())
        )
        val observer = mock<Observer<Int>>()
        deliveryViewModel.deliveryBoundaryCallback.error.observeForever(observer)
        deliveryViewModel.deliveryBoundaryCallback.onZeroItemsLoaded()
        val inOrder = Mockito.inOrder(observer)
        inOrder.verify(observer).onChanged(AlertMessage.ERROR.message)
    }

    @Test
    fun onNetworkErrorTest() {
        given(deliveryRepository.getDeliveries(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).willReturn(
            Single.error(ConnectException())
        )
        val observer = mock<Observer<Int>>()
        deliveryViewModel.deliveryBoundaryCallback.error.observeForever(observer)
        deliveryViewModel.deliveryBoundaryCallback.onZeroItemsLoaded()
        val inOrder = Mockito.inOrder(observer)
        inOrder.verify(observer).onChanged(AlertMessage.NETWORK_ERROR.message)
    }

    @Test
    fun onRefreshTest() {
        given(deliveryRepository.getDeliveries(any(), any())).willReturn(Single.just(mock()))

        deliveryViewModel.isLoading.value?.let { assert(it) }
        deliveryViewModel.onRefresh()

        val spy = spy(deliveryViewModel.deliveryBoundaryCallback)
        assert(deliveryViewModel.isLoading.value!!)

        spy.onRefresh()
        verify(spy, times(1)).onZeroItemsLoaded()
    }





    @Test
    fun onGetItemAgainstIdTest() {

        val delivery = Delivery(
            1,
            "Test text",
            "https://s3-ap-southeast-1.amazonaws.com/lalamove-mock-api/images/pet-3.jpeg",
            Location(1.1, 1.1, "")
        )
        given(deliveryRepository.getDelivery(ArgumentMatchers.anyInt()))
            .willReturn(Single.just(delivery))

        assert(deliveryViewModel.disposables.size() == 0)
        assert(deliveryViewModel.delivery.value == null)
        deliveryViewModel.getItemDescription(ArgumentMatchers.anyInt())
        assert(deliveryViewModel.disposables.size() == 1)
        verify(deliveryRepository, never()).removeAll()
        verify(deliveryRepository, never()).getCount()
        verify(deliveryRepository, never()).getDeliveries(any(), any())
        deliveryRepository.getDelivery(1).test().assertValue(delivery)

        val deliveryResponse = deliveryRepository.getDelivery(ArgumentMatchers.anyInt()).blockingGet()
        assert(delivery == deliveryResponse)

    }


    @After
    fun tearDown() {
    }


}