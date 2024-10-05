package com.example.mungge_groom.ui.home

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.DrawableRes
import com.example.mungge_groom.databinding.ToastRunComplateBinding

class CustomToast(private val context: Context) {
    fun showToast(
        message: String,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        val inflater = LayoutInflater.from(context)
        val binding = ToastRunComplateBinding.inflate(inflater)

        // Set up custom view for the toast
        binding.toastTitle.text = message


        // Using 'var' to allow reassignment if needed
        val toast = Toast(context).apply {
            this.duration = duration
            setGravity(Gravity.CENTER, 0, 0)
            view = binding.root // Set the custom view
        }

        toast.show()
    }
}
