package com.chaitanya.android.delivery.model.repository.databse.entity

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class Location(
    val lat: Double,
    val lng: Double,
    val address: String
)