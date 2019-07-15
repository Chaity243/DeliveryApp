package com.chaitanya.android.delivery.view

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity<T : ViewModel> : DaggerAppCompatActivity() {

    protected  var viewModel: T? = null

    /**
     *
     * @return view model instance
     */
    abstract fun getActivityViewModel(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewModel = if (viewModel == null) getActivityViewModel() else viewModel
    }
}