package com.example.mungge_groom.ui.chat


import android.util.Log
import android.view.View
import com.example.mungge_groom.R
import com.example.mungge_groom.data.response.ChatRoom
import com.example.mungge_groom.databinding.ItemChatRoomBinding
import com.example.mungge_groom.ui.base.BaseAdapter
import com.example.mungge_groom.ui.base.BaseDiffCallback
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ChatRoomAdapter(private val userId: String) : BaseAdapter<ChatRoom, ItemChatRoomBinding> (
    BaseDiffCallback(
        itemsTheSame = { oldItem, newItem -> oldItem == newItem },
        contentsTheSame = { oldItem, newItem -> oldItem == newItem }
    )
) {
    override val layoutId: Int
        get() = R.layout.item_chat_room

    override fun bind(binding: ItemChatRoomBinding, item: ChatRoom) {
        with(binding) {
            // 보낸 사람의 아이디와 나의 아이디를 비교하여 메시지 위치 결정
            if (userId == item.receiver_id) {
Log.d("유저","$userId, ${item.sender_id}")
                // 내가 보낸 메시지
                textView8.visibility = View.GONE
                textView9.visibility = View.GONE
                reT.visibility = View.GONE
                textView6.text = item.message
                textView7.text = formatTimeFromDate(item.created_at)
            } else {
                textView6.visibility = View.GONE
                textView7.visibility = View.GONE
                imageView.visibility = View.GONE
                textView8.text = item.message
                textView9.text = formatTimeFromDate(item.created_at)
            }
        }
    }

    private fun formatTimeFromDate(dateString: String): String {
        // ISO 8601 형식의 날짜 문자열을 Date 객체로 변환하기 위한 포맷터
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")

        // Date 객체로 변환
        val date = isoFormat.parse(dateString)

        // 시간과 분을 "HH:mm" 형식으로 변환하기 위한 포맷터
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(date)
    }
}