package com.example.mungge_groom.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mungge_groom.data.repository.ChatRepository
import com.example.mungge_groom.data.request.SendChatDTO
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel(){

    private val _chatSend = MutableStateFlow(BaseResponse<String>())
    val chatSend : StateFlow<BaseResponse<String>> = _chatSend

    private val _chatMessage = MutableStateFlow(BaseResponse<String>())
    val chatMessage : StateFlow<BaseResponse<String>> = _chatMessage

    private fun postSendChat(sendChatDTO: SendChatDTO){
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

    private fun getChatMessage(senderId: String,receiverId: String ){
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