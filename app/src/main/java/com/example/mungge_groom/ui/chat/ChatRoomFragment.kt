package com.example.mungge_groom.ui.chat

import android.os.Build
import android.util.Log
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
import com.example.mungge_groom.utils.SocketManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import io.socket.client.Socket
import io.socket.emitter.Emitter

@AndroidEntryPoint
class ChatRoomFragment : BaseActivity<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {
    private val chatViewModel: ChatViewModel by viewModels()
    private val mypageViewModel: MypageViewModel by viewModels()
    lateinit var name: String
    lateinit var chatRoomAdapter: ChatRoomAdapter
    var me: User? = null
    var you: User? = null
    private lateinit var mSocket: Socket

    private val onConnect = Emitter.Listener {
        runOnUiThread {
            // 연결 성공 시 처리
        }
    }

    private val onNewMessage = Emitter.Listener { args ->
        runOnUiThread {
            val message = args[0] as String
            chatViewModel.postSendChat(
                SendChatDTO(you?.user_id.toString(), me?.user_id.toString(), message)
            )
            chatViewModel.getChatMessage(
                you?.user_id.toString(), me?.user_id.toString())
        }
    }
    private fun formatTimeFromDate(dateString: String): String {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = isoFormat.parse(dateString)
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(date)
    }
    // 메시지 전송 예시
    private fun sendMessage(message: String) {
        mSocket.emit("chatMessage", message)
        chatViewModel.postSendChat(
            SendChatDTO(me?.user_id.toString(), you?.user_id.toString(), message)
        )
        chatViewModel.getChatMessage(
            me?.user_id.toString(), you?.user_id.toString())
        binding.et.text.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
        mSocket.off("chatMessage", onNewMessage)
        SocketManager.closeConnection()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun setLayout() {
        SocketManager.setSocket()
        mSocket = SocketManager.getSocket()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("chatMessage", onNewMessage)
        SocketManager.establishConnection()
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
                    it.messages.map { formatTimeFromDate(it.created_at) }
                    chatRoomAdapter.submitList(it.messages)
                    binding.fragmentChatRoomRv.adapter = chatRoomAdapter
                    binding.fragmentChatRoomRv.scrollToPosition(chatRoomAdapter.itemCount - 1)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnClicked() {
        binding.fragmentChatRoomSendBt.setOnClickListener {
            val message = binding.et.text.toString()
            if (message.isNotBlank()) {
                sendMessage(message)
                Log.d("okhttp","$me, $you")
            }
        }
    }
}