package com.chaitanya.android.delivery.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.chaitanya.android.delivery.BuildConfig
import com.chaitanya.android.delivery.model.repository.IDeliveryRepository
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.chaitanya.android.delivery.pagination.DeliveryBoundaryCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DeliveryViewModel @Inject constructor(
    private var deliveryRepository: IDeliveryRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()
    val isLoading =MutableLiveData<Boolean>()
    val tvErrorVisibility = MutableLiveData<Boolean>()
    val tvError = MutableLiveData<String>()
    var deliveryList: LiveData<PagedList<Delivery>>
    var delivery = MutableLiveData<Delivery>()
    var deliveryBoundaryCallback: DeliveryBoundaryCallback

    init {
        val config = PagedList.Config.Builder()
            .setPrefetchDistance(0)
            .setPageSize(BuildConfig.PAGE_SIZE)
            .setInitialLoadSizeHint(BuildConfig.PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()

        deliveryBoundaryCallback =
            DeliveryBoundaryCallback(deliveryRepository, disposables)
        deliveryList = LivePagedListBuilder(deliveryRepository.getDeliveries(), config)
            .setBoundaryCallback(deliveryBoundaryCallback).build()
    }


    fun getItemDescription(id: Int) {
        disposables.add(
            deliveryRepository
                .getDelivery(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    delivery.value=result
                }
        )
    }



    fun onRefresh() {
        isLoading.value =true
        tvErrorVisibility.value=false
        deliveryBoundaryCallback.onRefresh()
    }

    fun retry() = deliveryBoundaryCallback.retry()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}