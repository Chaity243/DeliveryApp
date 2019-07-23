package com.chaitanya.android.delivery.model.repository

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.chaitanya.android.delivery.model.repository.databse.DeliveryDao
import com.chaitanya.android.delivery.model.repository.databse.DeliveryDatabase
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.chaitanya.android.delivery.model.repository.databse.entity.Location
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class DeliveryDatabseTest {
    private lateinit var db: DeliveryDatabase
    private lateinit var deliveryDao: DeliveryDao
    private lateinit var deliveryList: List<Delivery>
    private lateinit var deliveryItem: Delivery




    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            DeliveryDatabase::class.java
        ).build()
        deliveryDao = db.deliveryDao()
        deliveryList=ArrayList<Delivery>()
        deliveryItem = Delivery(
            1,
            "test Description",
            "https://s3-ap-southeast-1.amazonaws.com/lalamove-mock-api/images/pet-3.jpeg",
            Location(
                22.3,
                114.7,
                "Test address"
            )
        )

        for(i in 0..5)
        {
            (deliveryList as ArrayList<Delivery>).add(
                Delivery(
                    i,
                    "test Description$i",
                    "https://s3-ap-southeast-1.amazonaws.com/lalamove-mock-api/images/pet-3$i.jpeg",
                    Location(
                        i.toDouble(),
                        i.toDouble(),
                        "Test address$i"
                    )
                )
            )
        }

    }




    @Test
    fun getAllDeliveries(){

        deliveryDao.clearAll()
        deliveryDao.getDeliveryItemsCount().test().assertValue(0)

        //  Adding list data to DB
        deliveryDao.insertAllDeliveryItems(deliveryList)
        deliveryDao.getDeliveryItemsCount().test().assertValue(deliveryList.size)
    }

    @Test
    fun getDeliveryAgainstId() {
        deliveryDao.clearAll()
        deliveryDao.getDeliveryItemsCount().test().assertValue(0)



        //  Adding list data to DB
        deliveryDao.insertAllDeliveryItems(deliveryList)

        val i =2
        deliveryDao.getDeliveryAgainstId(deliveryList[i].id).test().assertValue(deliveryList[i])

    }



    @Test
    fun insertSingleDeliveryItem() {
        deliveryDao.clearAll()
        deliveryDao.getDeliveryItemsCount().test().assertValue(0)

        //  Adding list data to DB
        deliveryDao.insertSingleDeliveryItem(deliveryItem)
        deliveryDao.getDeliveryItemsCount().test().assertValue(1)
    }



    @After
    fun tearDown() {
        db.close()
    }
}