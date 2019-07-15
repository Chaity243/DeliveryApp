package com.chaitanya.android.delivery.dagger.module

import com.chaitanya.android.delivery.BuildConfig
import com.chaitanya.android.delivery.model.repository.network.DeliveryAPI
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
        client: OkHttpClient
    ): Retrofit {


        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideDeliveryAPI(retrofit: Retrofit): DeliveryAPI = retrofit
        .create(DeliveryAPI::class.java)

    @Provides
    @Singleton
    fun getGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create(Gson())

    @Provides
    @Singleton
    fun getRxJavaAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Provides
    @Singleton
    fun getOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}
