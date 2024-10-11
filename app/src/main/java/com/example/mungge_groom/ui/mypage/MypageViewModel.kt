package com.example.mungge_groom.ui.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mungge_groom.data.repository.MypageRepository
import com.example.mungge_groom.data.request.MatchesDTO
import com.example.mungge_groom.data.response.User
import com.example.mungge_groom.data.response.UserResponse
import com.example.mungge_groom.ui.base.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MypageViewModel @Inject constructor(
    private val mypageRepository: MypageRepository
) : ViewModel(){
    private val _getUsersData = MutableStateFlow<List<User>>(emptyList())
    val getUsersData: StateFlow<List<User>> = _getUsersData
    fun getUsers() {
        viewModelScope.launch {
            try {
                mypageRepository.getUsers().collect {
                    _getUsersData.value = it
                }
            } catch (e: Exception) {
                Log.e("Get Users Error", e.message.toString())
            }
        }
    }

}