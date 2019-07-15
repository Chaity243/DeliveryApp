package com.chaitanya.android.delivery.util

import com.chaitanya.android.delivery.R

enum class AlertMessage(var message :Int)  {
    ERROR(R.string.error),
    NETWORK_ERROR(R.string.network_error),
    LOADED (R.string.no_more_data)
}