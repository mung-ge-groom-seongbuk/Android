package com.example.mungge_groom.ui.account

import com.example.mungge_groom.ui.home.MainActivity
import com.example.mungge_groom.R
import com.example.mungge_groom.ui.base.BaseActivity
import com.example.mungge_groom.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    override fun setLayout() {
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.loginBtn.setOnClickListener {
            startActivityWithClear(MainActivity::class.java)
        }
    }
}