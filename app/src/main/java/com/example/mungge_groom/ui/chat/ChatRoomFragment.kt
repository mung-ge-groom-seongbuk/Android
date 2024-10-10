package com.example.mungge_groom.ui.chat

import android.os.Build
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mungge_groom.R
import com.example.mungge_groom.data.request.SendChatDTO
import com.example.mungge_groom.data.response.ChatRoom
import com.example.mungge_groom.data.response.User
import com.example.mungge_groom.databinding.FragmentChatRoomBinding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.base.BaseActivity
import com.example.mungge_groom.ui.mypage.MypageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Locale
import java.util.TimeZone


@AndroidEntryPoint
class ChatRoomFragment : BaseActivity<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {
    private val chatViewModel: ChatViewModel by viewModels()
    private val mypageViewModel: MypageViewModel by viewModels()
    lateinit var name: String
    lateinit var chatRoomAdapter: ChatRoomAdapter
    var me: User? = null
    var you: User? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setLayout() {
        initSetting()
        setOnClicked()
    }

    private fun initSetting() {
        name = intent?.getStringExtra("name").toString()
        binding.fragmentChatRoomTb.title = name
        mypageViewModel.getUsers()
        setMe()
        setYou()
        setRoom()
    }

    private fun setMe() {
        lifecycleScope.launch {
            GlobalApplication.instance.tokenManager.getUser(this@ChatRoomFragment)
                .collect { users ->
                    if (users != null) {
                        me = users  // user 초기화
                    }
                }
        }
    }

    private fun setYou() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                mypageViewModel.getUsersData.collectLatest { user ->
                    you = user.find { it.nickname == name }
                    chatRoomAdapter = ChatRoomAdapter(me?.user_id.toString())
                    chatViewModel.getChatMessage(me?.user_id.toString(), you?.user_id.toString())
                }
            }
        }
    }

    private fun setRoom() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                chatViewModel.chatMessage.collectLatest {
                    chatRoomAdapter.submitList(it.messages)
                    binding.fragmentChatRoomRv.scrollToPosition(chatRoomAdapter.itemCount - 1)
                    binding.fragmentChatRoomRv.adapter = chatRoomAdapter
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClicked() {
        binding.fragmentChatRoomSendBt.setOnClickListener {
            val message = binding.et.text.toString()
            chatViewModel.postSendChat(
                SendChatDTO(me?.user_id.toString(), you?.user_id.toString(), message)
            )
            chatViewModel.getChatMessage(
                me?.user_id.toString(), you?.user_id.toString())
            binding.et.text.clear()
        }
    }


}