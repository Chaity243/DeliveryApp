package com.chaitanya.android.delivery.databinding

import android.databinding.BindingAdapter
import android.databinding.BindingConversion
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.chaitanya.android.delivery.BuildConfig
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso


@BindingConversion
fun setVisibility(state: Boolean): Int {
    return if (state) View.VISIBLE else View.GONE
}

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Picasso.get().load(url).into(imageView)
    }
}

@BindingAdapter("initMap")
fun addMarkerToMap(mapView: MapView, delivery: Delivery?) {
    if (delivery != null) {
        mapView.onCreate(Bundle())
        mapView.getMapAsync { googleMap ->
            MapsInitializer.initialize(mapView.context)

            // Add a marker using specified lat lang  and move the camera
            val latLng = LatLng(delivery.location.lat, delivery.location.lng)

            //To just change the zoom value to any desired value between minimum value=2.0 and maximum value=21.0.
            //
            //The API warns that not all locations have tiles at values at or near maximum zoom.
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, BuildConfig.DEFAULT_MAP_ZOOM))
            googleMap.addMarker(MarkerOptions().position(latLng).title(delivery.location.address))
            mapView.onResume()
        }
    }
}
