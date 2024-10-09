package com.example.mungge_groom.ui.account

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mungge_groom.R
import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.databinding.ActivityLoginBinding
import com.example.mungge_groom.extention.GlobalApplication
import com.example.mungge_groom.ui.base.BaseActivity
import com.example.mungge_groom.ui.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    var token : String? = ""
    private val accountViewModel: AccountViewModel by viewModels()
    override fun setLayout() {
        setOnClickListener()
        observeLifeCycle()
        lifecycleScope.launch {
            token = GlobalApplication.instance.tokenManager.getFireBaseTokenIdId().first()
        }
    }

    private fun setOnClickListener() {

        binding.loginKakaoLoginBt.setOnClickListener{
            val intent = Intent(this@LoginActivity,StartActivity::class.java)
            startActivity(intent)
        }
        binding.loginLoginBt.setOnClickListener {
            if(!token.isNullOrEmpty()) {
                accountViewModel.postLogIn(
                    LogInDTO(
                        binding.loginUsernameInputEt.text.toString(),
                        binding.loginPasswordInputEt.text.toString(),
                        token.toString()
                    )
                )
                Log.d("토큰s","$token")
            }
        }
        binding.loginSignupTv.setOnClickListener {
            startNextActivity(SignUpActivity::class.java)
        }
    }

    private fun observeLifeCycle() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                accountViewModel.logInData.collectLatest {
                    if (it.message.isNotEmpty()) {
                        Log.d("메세지", "Received login data: ${it.result}, Message: ${it.message}")
                        if (it.message == "로그인 성공!") {
                            startActivityWithClear(MainActivity::class.java)
                        } else {
                            showToast(it.message)
                        }
                    }
                }
            }
        }
    }
}