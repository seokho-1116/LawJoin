package com.example.lawjoin.data.model

import java.time.Instant

class  Reservation(
    val fromDate: Instant,
    val toDate: Instant,
    val userId: Int,
    val lawyerId: Int,
) {
}