package com.example.mungge_groom.ui.account

import com.example.mungge_groom.R
import com.example.mungge_groom.databinding.ActivitySignUpBinding
import com.example.mungge_groom.ui.base.BaseActivity

class SignUpActivity : BaseActivity<ActivitySignUpBinding>(R.layout.activity_sign_up) {
    override fun setLayout() {
        setOnClickListener()
    }

    private fun setOnClickListener() {

        binding.signUpSignUpBt.setOnClickListener {
            startActivityWithClear(LoginActivity::class.java)
        }
    }
}