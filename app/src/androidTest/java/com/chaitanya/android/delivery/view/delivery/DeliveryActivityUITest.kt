package com.chaitanya.android.delivery.view.delivery

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.Toolbar
import com.chaitanya.android.delivery.R
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.chaitanya.android.delivery.model.repository.databse.entity.Location
import com.chaitanya.android.delivery.view.delivery.CustomRecycerViewMatcher.Companion.hasDeliveryDataforPosition
import com.chaitanya.android.delivery.view.delivery.adapter.DeliveryListAdapter
import com.chaitanya.android.delivery.view.util.MockServer
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DeliveryActivityUITest {

    private lateinit var activity: DeliveryActivity
    private lateinit var mockServer: MockWebServer


    private var deliveryList = mutableListOf<Delivery>()
    lateinit var adapter :DeliveryListAdapter


    @Before
    fun createDeliveryList()
    {
      for(i in 0..10)
      {
          deliveryList.add(i, Delivery(i, "item$i","", Location(i.toDouble(),i.toDouble(), "item$i")))
      }

        adapter = DeliveryListAdapter()
        Intents.init()

    }

    @Before
    fun init()
    {
        mockServer = MockWebServer()
        mockServer.play(8181)
        mockServer.enqueue(MockServer.response("json/deliveries.json"))
        activity = activityActivityTestRule.launchActivity(Intent())
    }


    @Rule
    @JvmField
    var activityActivityTestRule = ActivityTestRule(DeliveryActivity::class.java)

    @Test
    fun swipeRefreshLayoutTest_shouldShowSwiping()
    {
        onView(withId(R.id.swipeRefreshLayout))
            .perform(swipeDown())
            .check(matches(isDisplayed()))

    }

    @Test
    fun testToolbarTest_shouldHaveCorrectAppName()
    {
        onView(allOf(withParent(isAssignableFrom(Toolbar::class.java)),isAssignableFrom(AppCompatTextView::class.java)))
            .check(matches(withText(R.string.app_name)))
    }

    @Test
    fun testRecyclerView_shouldHaveCorrectData()
    {

        /*adapter.submitList(mockPagedList(deliveryList))*/
        onView(withId(R.id.recyclerView))
            .check(matches(hasDeliveryDataforPosition(0,deliveryList[0])))
    }

    @Test
    fun testDataSuccess()
    {

        assert(activity.adapter.itemCount == 4)

    }

}