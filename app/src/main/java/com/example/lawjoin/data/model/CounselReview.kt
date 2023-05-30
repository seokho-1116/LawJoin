package com.example.lawjoin.data.model

import java.time.ZonedDateTime

class CounselReview(
    val title: String,
    val detail: String,
    val createdTime: ZonedDateTime,
    val writerId: String?,
    val lawyerId: String?
) {
}