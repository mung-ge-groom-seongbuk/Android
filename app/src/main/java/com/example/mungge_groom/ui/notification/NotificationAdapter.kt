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
import com.example.mungge_groom.ui.listener.onClickSendMatchListener

class NotificationAdapter() : BaseAdapter<NotificationData, ItemNotificationBinding>(
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
        binding.appCompatButton.setOnClickListener {
            val cl = item
            cl.isPermission = false
            binding.itemNotificationDescTv.text = ""
            binding.itemNotificationTitleTv.text = "${GlobalApplication.instance.user?.nickname}님이 요청을 수락했어요!"

            // Get the current list and add the new item to the front
            val list = currentList
            val lists = mutableListOf<NotificationData>().apply {
                addAll(list)
                add(cl)
            }
            binding.appCompatButton.visibility = View.GONE
            // Update the list
            submitList(lists)
        }
        GlobalApplication.loadProfileImage(binding.itemNotificationProfileIv, item.profile)
    }


}