package com.example.mungge_groom.ui.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mungge_groom.data.repository.AccountRepository
import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.ui.base.BaseResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _logInData = MutableStateFlow(BaseResponse<String>())
    val logInData: StateFlow<BaseResponse<String>> = _logInData

    private val _signUpData = MutableStateFlow(BaseResponse<String>())
    val signUpData: StateFlow<BaseResponse<String>> = _signUpData

    private val _updateProfileData = MutableStateFlow(BaseResponse<String>())
    val updateProfileData: StateFlow<BaseResponse<String>> = _updateProfileData

    fun postLogIn(logInDTO: LogInDTO) {
        viewModelScope.launch {
            try {
                accountRepository.postLogIn(logInDTO).collect {
                    _logInData.value = it
                }
            } catch (e: Exception) {
                Log.e("Login Error", e.message.toString())
            }
        }
    }

    fun postSignUp(signUpDTO: SignUpDTO) {
        viewModelScope.launch {
            try {
                accountRepository.postSignUp(signUpDTO).collect {
                    _signUpData.value = it
                }
            } catch (e: Exception) {
                Log.e("SignUp Error", e.message.toString())
            }
        }
    }

    fun postUpdateProfile(
        fileUri: MultipartBody.Part,  // 프로필 사진의 URI
        intro: RequestBody,
        nickname: RequestBody,
        email: RequestBody
    ) {
        viewModelScope.launch {
            try {
                accountRepository.postUpdateProfile(fileUri, intro, nickname, email).collect {
                    _signUpData.value = it
                }
            } catch (e: Exception) {
                Log.e("SignUp Error", e.message.toString())
            }
        }
    }
}