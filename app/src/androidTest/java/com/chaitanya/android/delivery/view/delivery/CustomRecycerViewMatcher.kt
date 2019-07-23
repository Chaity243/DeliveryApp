package com.chaitanya.android.delivery.view.delivery

import android.support.test.espresso.matcher.BoundedMatcher
import android.support.test.espresso.matcher.ViewMatchers.withChild
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.text.FieldPosition

class CustomRecycerViewMatcher {
    companion object{

        fun hasDeliveryDataforPosition(position: Int, delivery: Delivery): Matcher<in View>? {

            return object :BoundedMatcher<View, RecyclerView>(RecyclerView::class.java)
            {
                override fun describeTo(description: Description?) {
                  description?.appendText("OH! Item at position :"+position +" is " + delivery.description)
                }
                override fun matchesSafely(recyclerView: RecyclerView?): Boolean {

                  val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position) ?: return false

                    return withChild(withChild(withText(delivery.description))).matches(viewHolder.itemView)
                }
            }

        }
    }
}