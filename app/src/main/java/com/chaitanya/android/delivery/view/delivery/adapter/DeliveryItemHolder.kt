package com.chaitanya.android.delivery.view.delivery.adapter

import android.support.v7.widget.RecyclerView
import com.chaitanya.android.delivery.databinding.DeliveryViewItemBinding
import com.chaitanya.android.delivery.model.repository.databse.entity.Delivery


class DeliveryItemHolder(
    private val binding: DeliveryViewItemBinding,
    private val onItemClickListener: ((item: Delivery) -> Unit)?
) :
    RecyclerView.ViewHolder(binding.root) {

    lateinit var delivery: Delivery

    fun bind(delivery: Delivery) {
        this.delivery = delivery

        binding.viewModel = delivery
        binding.root.setOnClickListener { onItemClickListener?.invoke(delivery) }
    }
}