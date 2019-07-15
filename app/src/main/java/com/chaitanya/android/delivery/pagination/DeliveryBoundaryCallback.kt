@file:Suppress("KotlinDeprecation")

package com.chaitanya.android.delivery.pagination

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.chaitanya.android.delivery.BuildConfig
import com.chaitanya.android.delivery.model.repository.IDeliveryRepository
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.chaitanya.android.delivery.model.repository.databse.entity.DeliveryResult
import com.chaitanya.android.delivery.util.AlertMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException

@Suppress("UNCHECKED_CAST")
class DeliveryBoundaryCallback(
    private val deliveryRepository: IDeliveryRepository,
    private var disposable: CompositeDisposable
) :
    PagedList.BoundaryCallback<Delivery>() {

    private var totalCount: Int = 0
    private var isLoaded: Boolean = false
    var error = MutableLiveData<Int>()
    var loading = MutableLiveData<Boolean>()
    var success = MutableLiveData<Boolean>()
    var loaded = MutableLiveData<Int>()


    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    override fun onZeroItemsLoaded() {
        updateLoadingState()
        fetchNetwork(0, BuildConfig.PAGE_SIZE)

    }


    /**
     * When all items from the database were loaded, we need to query the backend for more items.
     */
    override fun onItemAtFrontLoaded(itemAtFront: Delivery) {
        super.onItemAtFrontLoaded(itemAtFront)

        disposable.add(deliveryRepository.getCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result ->
                if (totalCount < result) totalCount = result
            })

    }


    /*  BoundaryCallback does this signaling - when a DataSource runs out of data at the end of the list*/
    override fun onItemAtEndLoaded(itemAtEnd: Delivery) {
        super.onItemAtEndLoaded(itemAtEnd)
        if (!isLoaded) {
            updateLoadingState()
            fetchNetwork(totalCount, BuildConfig.PAGE_SIZE)
        }
    }

    private fun fetchNetwork(offset: Int, limit: Int) {
        disposable.add(
            deliveryRepository.getDeliveries(offset, limit)
                .toObservable()
                .map { DeliveryResult.Success(it) as DeliveryResult }
                .onErrorReturn { DeliveryResult.Failure(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    when (result) {
                        is DeliveryResult.Success -> {
                            success(result.response as List<Delivery>)
                        }
                        is DeliveryResult.Failure -> {
                            error(result.throwable)
                        }
                    }
                }, { e -> error(e) })
        )
    }

    private fun success(list: List<Delivery>) {
        totalCount += list.size
        if (list.size < BuildConfig.PAGE_SIZE) {
            isLoaded = true
            this.loaded.postValue(AlertMessage.LOADED.message)
        } else
            this.success.postValue(true)

    }


    private fun error(throwable: Throwable) {
        if (throwable is ConnectException) {
            updateErrorState(AlertMessage.NETWORK_ERROR.message)
        } else {
            updateErrorState(AlertMessage.ERROR.message)
        }

    }

    private fun updateErrorState(error: Int) {
        this.error.postValue(error)

    }


    fun retry() {
        updateLoadingState()
        fetchNetwork(totalCount, BuildConfig.PAGE_SIZE)
    }

    private fun updateLoadingState() {
        this.loading.postValue(true)

    }

    fun onRefresh() {
        totalCount = 0
        isLoaded = false

        disposable.clear()
        onZeroItemsLoaded()
    }
}