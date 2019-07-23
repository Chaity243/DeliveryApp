package com.chaitanya.android.delivery.view.map

import android.R
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.chaitanya.android.delivery.model.repository.databse.entity.Location
import com.chaitanya.android.delivery.viewmodel.DeliveryViewModel
import com.nhaarman.mockito_kotlin.given
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.fakes.RoboMenuItem


@RunWith(RobolectricTestRunner::class)
class MapActivityTest {

    private lateinit var activity: MapActivity
    @Mock
    private lateinit var viewModel: DeliveryViewModel

    @Before
    fun setUp() {
        activity = Robolectric.buildActivity(MapActivity::class.java)
            .create()
            .resume()
            .get()

        viewModel = Mockito.mock(DeliveryViewModel::class.java)
    }


    @Test
    fun deliveryFromViewModelTest() {
        val delivery = Delivery(
            1,
            "Test Value",
            "https://s3-ap-southeast-1.amazonaws.com/lalamove-mock-api/images/pet-3.jpeg",
            Location(1.1, 1.1, "")
        )

        var deliveryItem = MutableLiveData<Delivery>()
        deliveryItem.value=delivery
        given(viewModel.delivery).willReturn(deliveryItem)
        viewModel.getItemDescription(1)
        assert(viewModel.delivery.value?.description.equals("Test Value"))

    }

    @Test
    fun onBackButtonPressedTest() {
        val menuItem = RoboMenuItem(R.id.home)
        activity.onOptionsItemSelected(menuItem)
        assert(activity.isFinishing)
    }
}