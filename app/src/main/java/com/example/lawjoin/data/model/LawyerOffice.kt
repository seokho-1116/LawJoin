package com.example.lawjoin.data.model

import java.time.ZonedDateTime

class LawyerOffice (
    val name: String,
    val phone: String,
    val openingTime: ZonedDateTime,
    val closingTime: ZonedDateTime,
    val location: String,
)
