@file:Suppress("KotlinDeprecation")

package com.chaitanya.android.delivery.view.delivery

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.chaitanya.android.delivery.BuildConfig.BUNDLE_KEY_DELIVERY
import com.chaitanya.android.delivery.R
import com.chaitanya.android.delivery.databinding.ActivityDeliveryBinding
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery
import com.chaitanya.android.delivery.view.BaseActivity
import com.chaitanya.android.delivery.view.delivery.adapter.DeliveryListAdapter
import com.chaitanya.android.delivery.view.map.MapActivity
import com.chaitanya.android.delivery.viewmodel.DeliveryViewModel
import com.chaitanya.android.delivery.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_delivery.*
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject
import javax.inject.Named


class DeliveryActivity : BaseActivity<DeliveryViewModel>() {
    override fun getActivityViewModel(): DeliveryViewModel {
        return  ViewModelProviders.of(this, viewModelFactory).get(DeliveryViewModel::class.java)
    }

    @Inject
    @Named("DeliveryActivity")
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var adapter: DeliveryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDeliveryBinding = DataBindingUtil.setContentView(this, R.layout.activity_delivery)


        binding.viewModel = viewModel
        binding.lifecycleOwner=this
        adapter = DeliveryListAdapter()
        adapter.onItemClickListener = { onDeliveryItemClicked(it) }
        adapter.onLoadMoreClickListener = { viewModel?.retry() }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager?
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                DividerItemDecoration.VERTICAL
            )
        )

        observe()
    }

    private fun observe() {
        viewModel?.deliveryList?.observe(this, Observer {
            adapter.submitList(it)
            recyclerView.recycledViewPool.clear()
            adapter.notifyDataSetChanged()

        })


        //  loading
        viewModel?.deliveryBoundaryCallback?.loading?.observe(this, Observer {
            if(adapter.itemCount==0)  viewModel?.isLoading?.value=true
            else{
                adapter.setLoading(loading = true, loadMore = false)
                adapter.notifyItemChanged(adapter.itemCount - 1)
            }
        })



        // success
        viewModel?.deliveryBoundaryCallback?.success?.observe(this, Observer {
            adapter.setLoading(loading = false, loadMore = false)
            viewModel?.isLoading?.value=false
        })


        // error
        viewModel?.deliveryBoundaryCallback?.error?.observe(this, Observer {
            if(adapter.itemCount==0) {
                viewModel?.tvErrorVisibility?.value=true
                viewModel?.tvError?.value= it?.let { it -> getString(it) }
            }

            else it?.let {
                    it1 -> showAlert(it1).show() }

            viewModel?.isLoading?.value =false
            adapter.setLoading(false, adapter.itemCount != 0)
            adapter.notifyItemChanged(adapter.itemCount - 1)
        })

        // all data loaded
        viewModel?.deliveryBoundaryCallback?.loaded?.observe(this, Observer {
            adapter.setLoading(loading = false, loadMore = false)
            viewModel?.isLoading?.value=false
            it?.let { it -> showAlert(it).show() }
        })
    }

    fun onDeliveryItemClicked(delivery: Delivery) {

        val intent = Intent(this, MapActivity::class.java).putExtra(BUNDLE_KEY_DELIVERY, delivery.id)
        startActivity(intent)
    }

    fun showAlert(alertMessage: Int): AlertDialog = AlertDialog.Builder(this)
        .setMessage(getString(alertMessage))
        .setNeutralButton(getString(android.R.string.ok)) { dialog, _ -> dialog.dismiss() }
        .create()


    @TestOnly
    fun setTestViewModel(viewModel: DeliveryViewModel) {
        this.viewModel = viewModel
        observe()
    }

}
