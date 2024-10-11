package com.example.mungge_groom.data.model

import java.util.Date


data class RunningData(
    val userId: String,
    val distance: String,
    val duration: String,  // 시간은 초 단위로 저장한다고 가정
    val pace: String,
    val calories: String,
    val run_count : String = "1"
)