package com.example.mungge_groom.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mungge_groom.R
import com.example.mungge_groom.databinding.FragmentChatRoomBinding
import com.example.mungge_groom.ui.base.BaseActivity
import com.example.mungge_groom.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatRoomFragment : BaseActivity<FragmentChatRoomBinding>(R.layout.fragment_chat_room) {
    override fun setLayout() {
        binding.fragmentChatRoomTb.title = intent?.getStringExtra("name")
    }
}