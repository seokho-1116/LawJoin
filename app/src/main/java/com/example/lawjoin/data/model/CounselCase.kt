package com.example.lawjoin.data.model

import java.time.ZonedDateTime

class CounselCase(
    val id: Int,
    val title: String,
    val detail: String,
    val date: ZonedDateTime,
    val result: String
) {
}