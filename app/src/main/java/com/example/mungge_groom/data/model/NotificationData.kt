package com.example.mungge_groom.data.model

data class NotificationData(
    val profile : String,
    val distance : String = "5",
    val title : String = "수박에게 매칭요청이 왔어요!",
    val description : String = "오늘 저녁에 같이 러닝 어떠세요?",
    val arriveTime : String = "4",
    val isPermission : Boolean = true
)
