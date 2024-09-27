package com.example.mungge_groom.account

import com.example.mungge_groom.home.MainActivity
import com.example.mungge_groom.R
import com.example.mungge_groom.base.BaseActivity
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