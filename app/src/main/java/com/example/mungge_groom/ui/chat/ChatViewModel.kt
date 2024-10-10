package com.example.mungge_groom.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mungge_groom.data.repository.ChatRepository
import com.example.mungge_groom.data.request.SendChatDTO
import com.example.mungge_groom.data.response.ChatRoomData
import com.example.mungge_groom.data.response.ChatRoomResponse
import com.example.mungge_groom.ui.base.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel(){

    private val _chatSend = MutableStateFlow(ChatRoomResponse())
    val chatSend : StateFlow<ChatRoomResponse> = _chatSend

    private val _chatMessage = MutableStateFlow(ChatRoomData())
    val chatMessage : StateFlow<ChatRoomData> = _chatMessage

    fun postSendChat(sendChatDTO: SendChatDTO){
        viewModelScope.launch {
            try {
                chatRepository.postSendChat(sendChatDTO).collect{
                    _chatSend.value = it
                }
            }catch (e : Exception){
                Log.e("Chat Send Error", e.message.toString())

            }
        }
    }

    fun getChatMessage(senderId: String,receiverId: String ){
        viewModelScope.launch {
            try {
                chatRepository.getChatMessage(senderId,receiverId).collect{
                    _chatMessage.value = it
                }
            }catch (e : Exception){
                Log.e("Chat Message Error", e.message.toString())

            }
        }
    }


}