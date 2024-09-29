package com.example.mungge_groom.ui.home

import com.example.mungge_groom.R
import com.example.mungge_groom.data.response.Toggle1Data
import com.example.mungge_groom.databinding.ItemToggle1Binding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.base.BaseAdapter
import com.example.mungge_groom.ui.base.BaseDiffCallback

class Toggle1Adapter : BaseAdapter<Toggle1Data, ItemToggle1Binding> (
    BaseDiffCallback(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override val layoutId: Int
        get() = R.layout.item_toggle1

    override fun bind(binding: ItemToggle1Binding, item: Toggle1Data) {
        binding.toggle1Item = item
        GlobalApplication.loadProfileImage(binding.itemToggle1ProfileIv,item.profile)
    }
}