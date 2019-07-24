package com.chaitanya.android.delivery.view.util

import android.support.test.InstrumentationRegistry
import com.squareup.okhttp.mockwebserver.MockResponse

object MockServer {

    fun response(jsonPath :String) :MockResponse = MockResponse().setResponseCode(200).setBody(getJSON(jsonPath))

    private fun getJSON(jsonPath: String):String
    {

        val context = InstrumentationRegistry.getInstrumentation().context
        val inpStream = context.resources.assets.open(jsonPath)
        val buffReader = inpStream.bufferedReader()
        val stringBuilder = StringBuilder()
        buffReader.lines().forEach { stringBuilder.append(it) }
        return stringBuilder.toString()
    }
}