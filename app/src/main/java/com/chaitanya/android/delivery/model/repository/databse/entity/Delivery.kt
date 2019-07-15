package com.chaitanya.android.delivery.model.repository.databse.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Class which provides a model for Delivery
 * @constructor Sets all properties of the post
 * @property id the unique identifier of the delivery item
 * @property description the description details of the delivery item
 * @property location location of delivery item
 */


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@Entity
data class Delivery(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
    @Embedded(prefix = "delivery_") val location: Location
)



