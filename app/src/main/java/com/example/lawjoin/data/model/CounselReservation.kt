package com.example.lawjoin.data.model

import java.time.ZonedDateTime

class CounselReservation(
    val startTime: ZonedDateTime,
    val userId: String?,
    val lawyerId: String?,
    val summary: String
) {
}