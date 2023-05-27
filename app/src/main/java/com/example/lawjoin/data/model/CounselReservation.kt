package com.example.lawjoin.data.model

import java.time.ZonedDateTime

class CounselReservation(
    val startTime: ZonedDateTime,
    val userId: Int,
    val lawyerId: Int,
    val summary: String
) {
}