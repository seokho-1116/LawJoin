package com.example.lawjoin.data.model

import java.time.LocalDateTime

class CounselReview(
    val title: String,
    val detail: String,
    val createdTime: LocalDateTime,
    val writerName: String,
    val lawyerName: String
) {
}