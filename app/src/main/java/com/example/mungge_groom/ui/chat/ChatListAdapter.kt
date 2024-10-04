package com.example.mungge_groom.ui.chat

import com.example.mungge_groom.R
import com.example.mungge_groom.data.response.ChatListData
import com.example.mungge_groom.data.response.Toggle1Data
import com.example.mungge_groom.databinding.ItemChatListBinding
import com.example.mungge_groom.databinding.ItemToggle1Binding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.base.BaseAdapter
import com.example.mungge_groom.ui.base.BaseDiffCallback
import com.example.mungge_groom.ui.listener.onChatRoomClickedListener

class ChatListAdapter(private val onChatRoomClickedListener: onChatRoomClickedListener) : BaseAdapter<ChatListData, ItemChatListBinding> (
    BaseDiffCallback(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override val layoutId: Int
        get() = R.layout.item_chat_list

    private var originalList: List<ChatListData> = listOf() // 원본 리스트를 저장

    // 데이터를 설정하는 메서드
    fun setChatListData(dataList: List<ChatListData>) {
        originalList = dataList
        submitList(dataList)
    }

    override fun bind(binding: ItemChatListBinding, item: ChatListData) {
        binding.chatListItem = item
        binding.root.setOnClickListener{
            onChatRoomClickedListener.onClick(item.nickName)
        }
        GlobalApplication.loadProfileImage(binding.itemChatListIv,item.profile)
    }

    fun findChatData(query: String) {
        val filteredList = originalList.filter {
            it.nickName.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true)
        }
        submitList(filteredList)
    }
}