package com.example.lawjoin.data.model

import java.time.LocalDateTime

class Reservation(
    val fromDate: LocalDateTime,
    val toDate: LocalDateTime,
    val userId: Int,
    val lawyerId: Int,
) {
}