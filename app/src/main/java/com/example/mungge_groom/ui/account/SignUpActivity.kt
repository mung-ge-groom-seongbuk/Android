package com.example.mungge_groom.ui.account

import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mungge_groom.R
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.databinding.ActivitySignUpBinding
import com.example.mungge_groom.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {

    private val accountViewModel : AccountViewModel by viewModels()
    override fun setLayout() {
        setOnClickListener()
        observeLifeCycle()
    }

    private fun setOnClickListener() {
        binding.signUpSignUpBt.setOnClickListener {
            val name = binding.signUpUserNameEt.text.toString()
            val email = binding.signUpUserEmailEt.text.toString()
            val phone_number = binding.signUpPhoneNumberEt.text.toString()
            val password = binding.signUpPasswordEt.text.toString()
            val signUpDTO = SignUpDTO(
                name, password, email, phone_number
            )
            accountViewModel.postSignUp(signUpDTO)
        }
    }

    private fun observeLifeCycle(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                accountViewModel.signUpData.collectLatest {response ->
                    if (response.message.isNotEmpty()) {
                        if (response.message == "회원가입이 완료되었습니다.") {
                            Log.d("메세지", "$response")
                            startActivityWithClear(LoginActivity::class.java)
                            Toast.makeText(this@SignUpActivity, "회원가입 성공", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Log.d("메세지", "$response")
                            Toast.makeText(this@SignUpActivity, "회원가입 실패", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }
}