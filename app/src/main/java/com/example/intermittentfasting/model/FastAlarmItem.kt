package com.example.intermittentfasting.model

data class FastAlarmItem(
    val timeInMillis: Long,
    val title: String,
    val message: String,
    val targetHours: Int
)
