package com.example.lawjoin.data.model

import java.time.LocalDateTime

class CounselCase(
    val id: Int,
    val title: String,
    val detail: String,
    val date: LocalDateTime,
    val result: String
) {
}