package com.chaitanya.android.delivery.view.map

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import com.chaitanya.android.delivery.BuildConfig.BUNDLE_KEY_DELIVERY
import com.chaitanya.android.delivery.R
import com.chaitanya.android.delivery.databinding.ActivityMapsBinding
import com.chaitanya.android.delivery.view.BaseActivity
import com.chaitanya.android.delivery.viewmodel.ViewModelFactory
import com.chaitanya.android.delivery.viewmodel.DeliveryViewModel
import javax.inject.Inject


class MapActivity : BaseActivity<DeliveryViewModel>() {
    override fun getActivityViewModel(): DeliveryViewModel {
        return ViewModelProviders.of(this, viewModelFactory).get(DeliveryViewModel::class.java)

    }

    @Inject

    lateinit var viewModelFactory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMapsBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_maps
            )
        val ab = supportActionBar
        ab!!.setHomeButtonEnabled(true)
        ab.setDisplayHomeAsUpEnabled(true)


        viewModel?.getItemDescription(this.intent.getIntExtra(BUNDLE_KEY_DELIVERY, 0))
        binding.viewModel = viewModel
        binding.lifecycleOwner=this
    }

    // Support Action Bar Back navigation
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}