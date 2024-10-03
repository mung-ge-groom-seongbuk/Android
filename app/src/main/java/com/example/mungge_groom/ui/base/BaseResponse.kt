package com.example.mungge_groom.ui.base

data class BaseResponse<T>(
    val message : String = "",
    val result: T? = null
)
