package com.chaitanya.android.delivery.view.delivery

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import android.content.DialogInterface
import android.databinding.ObservableBoolean
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chaitanya.android.delivery.R
import com.chaitanya.android.delivery.model.repository.DeliveryRepository
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.chaitanya.android.delivery.model.repository.databse.entity.Location
import com.chaitanya.android.delivery.pagination.DeliveryBoundaryCallback
import com.chaitanya.android.delivery.view.delivery.adapter.DeliveryItemHolder
import com.chaitanya.android.delivery.view.delivery.adapter.DeliveryListAdapter
import com.chaitanya.android.delivery.view.delivery.adapter.ProgressViewHolder
import com.chaitanya.android.delivery.view.map.MapActivity
import com.chaitanya.android.delivery.viewmodel.DeliveryViewModel
import com.nhaarman.mockito_kotlin.*
import kotlinx.android.synthetic.main.activity_delivery.*
import kotlinx.android.synthetic.main.item_progressbar.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlertDialog

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class DeliveryActivityTest  {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var activity: DeliveryActivity
    private lateinit var activityController: ActivityController<DeliveryActivity>

    @Mock
    private lateinit var viewModel: DeliveryViewModel
    @Mock
    private lateinit var deliveryList: MutableLiveData<PagedList<Delivery>>
    @Mock
    private lateinit var state: MutableLiveData<String>
    @Mock
    private lateinit var isLoading: MutableLiveData<Boolean>

    @Mock
    private lateinit var error: MutableLiveData<Int>

    @Mock
    private lateinit var loaded: MutableLiveData<Int>
    @Mock
    private lateinit var deliveryBoundaryCallback: DeliveryBoundaryCallback
    @Mock
    private lateinit var adapter: DeliveryListAdapter

    @Captor
    private lateinit var deliveryListCaptor: ArgumentCaptor<Observer<PagedList<Delivery>>>
    @Captor
    private lateinit var stateCaptor: ArgumentCaptor<MutableLiveData<String>>


    @Captor
    private lateinit var errorCaptor: ArgumentCaptor<Observer<Int>>
    @Captor
    private lateinit var isLoadingCaptor: ArgumentCaptor<Observer<Boolean>>
    @Captor
    private lateinit var loadedCaptor: ArgumentCaptor<Observer<Int>>

    @Before
    fun setUp() {
        given(viewModel.deliveryList).willReturn(deliveryList)
        given(viewModel.deliveryBoundaryCallback).willReturn(deliveryBoundaryCallback)
     /*   given(viewModel.isLoading).willReturn(isLoading)
        given(viewModel.deliveryBoundaryCallback.error).willReturn(error)*/
        given(viewModel.deliveryBoundaryCallback.loaded).willReturn(loaded)

        activityController = Robolectric.buildActivity(DeliveryActivity::class.java)
        activity = activityController.get()
        activityController.create()
        activity.setTestViewModel(viewModel)
        activityController.start()
        activity.adapter = adapter

        //verify(isLoading).observe(ArgumentMatchers.any(LifecycleOwner::class.java), isLoadingCaptor.capture())
     //   verify(error).observe(ArgumentMatchers.any(LifecycleOwner::class.java), errorCaptor.capture())
        verify(loaded).observe(ArgumentMatchers.any(LifecycleOwner::class.java), loadedCaptor.capture())
     //   verify(state).observe(ArgumentMatchers.any(LifecycleOwner::class.java), stateCaptor.capture())
    }

    @Test
    fun viewTest() {
        assert(!activity.swipeRefreshLayout.isRefreshing)
        assert(activity.recyclerView.visibility == View.VISIBLE)
        assert(activity.recyclerView.itemDecorationCount == 1)
        assert(activity.recyclerView.layoutManager is LinearLayoutManager)
        assert(activity.recyclerView.getItemDecorationAt(0) is DividerItemDecoration)


        val deliveryRepository = Mockito.mock(DeliveryRepository::class.java)
        given(deliveryRepository.getDeliveries()).willReturn(mock())

        activity.onDeliveryItemClicked(mock<Delivery>())

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MapActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun dataSuccessTest() {
        val list = ArrayList<Delivery>()
        for (i in 0 until 10) {
            list.add(
                Delivery(
                    i + 1, "hi", "",
                    Location(1.0, 2.0, "")
                )
            )
        }
        val pagedList = mockPagedList(list)

        deliveryListCaptor.value.onChanged(pagedList)
        assert( viewModel?.deliveryBoundaryCallback?.loaded.value==R.string.no_more_data)
        verify(activity.adapter).submitList(pagedList)
        verify(activity.adapter, times(1)).setLoading(any(), any())
    }

    @Test
    fun dataErrorTest() {
        assert(ShadowAlertDialog.getShownDialogs().size == 1)
        assert(viewModel.isLoading.value==false)
        assert( viewModel?.deliveryBoundaryCallback?.error.value==R.string.error)
        verify(activity.adapter, times(2)).setLoading(any(), any())
    }



    @Test
    fun allDataLoadedTest() {
       // assert(viewModel.deliveryBoundaryCallback.loaded.value==R.string.no_more_data)
        loadedCaptor.value.onChanged(R.string.no_more_data)
        assert(ShadowAlertDialog.getShownDialogs().size == 1)
        verify(activity.adapter, times(1)).setLoading(any(), any())
    }

    @Test
    fun adapterTest() {
        val delivery1 = Delivery(
            1,
            "hi",
            "",
            Location(1.0, 2.0, "")
        )
        val delivery2 = Delivery(
            1,
            "hi",
            "",
            Location(1.0, 2.0, "")
        )
        assert(DeliveryListAdapter.DELIVERY_COMPARATOR.areContentsTheSame(delivery1, delivery2))
        assert(DeliveryListAdapter.DELIVERY_COMPARATOR.areItemsTheSame(delivery1, delivery2))

        val pagedList = ArrayList<Delivery>()
        pagedList.add(delivery1)
        pagedList.add(delivery2)

        val adapter = DeliveryListAdapter()
        adapter.onItemClickListener = mock()
        adapter.onLoadMoreClickListener = mock()
        adapter.submitList(mockPagedList(pagedList))
        val holder = adapter.onCreateViewHolder(activity.recyclerView, 0)
        if (holder is DeliveryItemHolder) {
            holder.bind(delivery1)
            assert(holder.delivery == delivery1)
            assert(holder.binding.tvDel.text.isEmpty())

            val list = ArrayList<Delivery>()
            list.add(delivery1)
            list.add(delivery2)
            adapter.currentList?.add(delivery1)
            adapter.currentList?.add(delivery2)
            holder.bind(delivery1)
            assert(holder.binding.viewModel == delivery1)
        }
        assert(adapter.getDeliveryItem(0) == delivery1)
    }

    @Test
    fun adapterProgressHolderTest() {
        val delivery1 = Delivery(
            1, "hi", "",
            Location(1.0, 2.0, "")
        )
        val pagedList = ArrayList<Delivery>()
        pagedList.add(delivery1)
        val adapter = DeliveryListAdapter()
        adapter.onItemClickListener = mock()
        adapter.onLoadMoreClickListener = mock()
        adapter.submitList(mockPagedList(pagedList))
        val holder = adapter.onCreateViewHolder(activity.recyclerView, 1)
        if (holder is ProgressViewHolder) {
            holder.bind(true, true)
            assert(holder.binding.progressBar.visibility == View.VISIBLE)
            assert(holder.binding.btnLoadMore.visibility == View.VISIBLE)

            adapter.setLoading(true, true)
            adapter.onBindViewHolder(holder, adapter.itemCount - 1)
            assert(holder.binding.loadMore)
            assert(holder.binding.loading)

            adapter.onBindViewHolder(holder, adapter.itemCount - 1, ArrayList())
            assert(holder.binding.loadMore)
            assert(holder.binding.loading)
        }
    }

    @Test
    fun viewModelFactoryTest() {
        val model: Any = activity.viewModelFactory.create(DeliveryViewModel::class.java)
        assert(model is DeliveryViewModel)
    }

    @Test
    fun showAlertTest() {
        val alert = activity.showAlert(R.string.network_error)
        alert.show()
        assert(alert.isShowing)
        alert.getButton(DialogInterface.BUTTON_POSITIVE).performClick()
        assert(!alert.isShowing)
    }

    private fun <T> mockPagedList(list: List<T>): PagedList<T> {
        val pagedList = Mockito.mock(PagedList::class.java) as PagedList<T>
        Mockito.`when`(pagedList.get(ArgumentMatchers.anyInt())).then { invocation ->
            val index = invocation.arguments.first() as Int
            list[index]
        }
        Mockito.`when`(pagedList.size).thenReturn(list.size)
        return pagedList
    }
}