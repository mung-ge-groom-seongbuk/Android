package com.example.mungge_groom.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mungge_groom.R
import com.example.mungge_groom.data.model.NotificationData
import com.example.mungge_groom.databinding.FragmentNotificationBinding
import com.example.mungge_groom.databinding.ItemNotificationBinding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.base.BaseAdapter
import com.example.mungge_groom.ui.base.BaseDiffCallback
import com.example.mungge_groom.ui.base.BaseFragment

class NotificationAdapter : BaseAdapter<NotificationData, ItemNotificationBinding>(
    BaseDiffCallback(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
)
{
    override val layoutId: Int
        get() = R.layout.item_notification

    override fun bind(binding: ItemNotificationBinding, item: NotificationData) {
        binding.notificationData = item
        binding.appCompatButton.setOnClickListener{

        }
        GlobalApplication.loadProfileImage(binding.itemNotificationProfileIv,item.profile)
    }

}