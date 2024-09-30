package com.example.mungge_groom.ui.chat

import com.example.mungge_groom.R
import com.example.mungge_groom.data.response.ChatListData
import com.example.mungge_groom.data.response.Toggle1Data
import com.example.mungge_groom.databinding.ItemChatListBinding
import com.example.mungge_groom.databinding.ItemToggle1Binding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.base.BaseAdapter
import com.example.mungge_groom.ui.base.BaseDiffCallback

class ChatListAdapter : BaseAdapter<ChatListData, ItemChatListBinding> (
    BaseDiffCallback(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override val layoutId: Int
        get() = R.layout.item_chat_list

    override fun bind(binding: ItemChatListBinding, item: ChatListData) {
        binding.chatListItem = item
        GlobalApplication.loadProfileImage(binding.itemChatListIv,item.profile)
    }
}