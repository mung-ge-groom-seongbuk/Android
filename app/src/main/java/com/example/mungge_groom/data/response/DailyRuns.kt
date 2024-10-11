package com.example.mungge_groom.data.response

data class DailyRuns(
    val dailyRecord: DailyRecord = DailyRecord(),
    val message: String = ""
)