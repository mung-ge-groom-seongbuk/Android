package com.example.mungge_groom.ui.chat

import android.view.View
import com.example.mungge_groom.R
import com.example.mungge_groom.data.response.ChatListData
import com.example.mungge_groom.data.response.ChatMessage
import com.example.mungge_groom.data.response.ChatRoom
import com.example.mungge_groom.data.response.ChatRoomData
import com.example.mungge_groom.data.response.ChatRoomResponse
import com.example.mungge_groom.data.response.Toggle1Data
import com.example.mungge_groom.data.response.User
import com.example.mungge_groom.databinding.ItemChatListBinding
import com.example.mungge_groom.databinding.ItemChatRoomBinding
import com.example.mungge_groom.databinding.ItemToggle1Binding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.base.BaseAdapter
import com.example.mungge_groom.ui.base.BaseDiffCallback
import com.example.mungge_groom.ui.listener.onChatRoomClickedListener
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ChatRoomAdapter(private val im : String) : BaseAdapter<ChatRoom, ItemChatRoomBinding> (
    BaseDiffCallback(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    val me : String get() = im
    override val layoutId: Int
        get() = R.layout.item_chat_room

    override fun bind(binding: ItemChatRoomBinding, item: ChatRoom) {
        with(binding) {
            if (me == item.sender_id) {
                textView6.visibility = View.VISIBLE
                textView7.visibility = View.VISIBLE
                imageView.visibility = View.VISIBLE
                textView8.visibility = View.GONE
                textView9.visibility = View.GONE
                reT.visibility = View.GONE
                textView6.text = item.message
                textView7.text = formatTimeFromDate(item.created_at)
            }
            else{
                textView6.visibility = View.GONE
                textView7.visibility = View.GONE
                imageView.visibility = View.GONE
                textView8.visibility = View.VISIBLE
                textView9.visibility = View.VISIBLE
                reT.visibility = View.VISIBLE
                textView8.text = item.message
                textView9.text = formatTimeFromDate(item.created_at)
            }
        }
    }
    fun formatTimeFromDate(dateString: String): String {
        // ISO 8601 형식의 날짜 문자열을 Date 객체로 변환하기 위한 포맷터
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")

        // Date 객체로 변환
        val date = isoFormat.parse(dateString)

        // 시간과 분을 "HH:mm" 형식으로 변환하기 위한 포맷터
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(date)
    }
    fun addAdapter(cr: ChatRoom) {
        // 현재 리스트를 MutableList로 복사
        val newList = currentList.toMutableList()

        // 새로운 항목 추가
        newList.add(cr)

        // 새로운 리스트로 RecyclerView 업데이트
        submitList(newList)
    }

}