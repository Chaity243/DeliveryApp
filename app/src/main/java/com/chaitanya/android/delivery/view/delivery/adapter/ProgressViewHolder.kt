package com.chaitanya.android.delivery.view.delivery.adapter

import android.support.v7.widget.RecyclerView
import com.chaitanya.android.delivery.databinding.ItemProgressbarBinding
import kotlinx.android.synthetic.main.item_progressbar.view.*

class ProgressViewHolder(
    private val binding: ItemProgressbarBinding,
    private val onLoadMoreClickListener: (() -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(isLoading: Boolean, isLoadingMore: Boolean) {
        binding.root.btnLoadMore.setOnClickListener { onLoadMoreClickListener?.invoke() }
        binding.loading = isLoading
        binding.loadMore = isLoadingMore
    }
}
