package com.rikkei.tranning.basekotlin.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<I>(
    binding: ViewDataBinding,
    itemClick: (I) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    protected var item: I? = null

    init {
        binding.root.setOnClickListener {
            item?.let { itemClick(it) }
        }
    }

    open fun bindData(item: I) {
        this.item = item
    }
}
