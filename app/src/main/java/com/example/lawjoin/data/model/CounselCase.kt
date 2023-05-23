package com.example.lawjoin.data.model

import java.time.Instant

class CounselCase(
    val id: Int,
    val title: String,
    val detail: String,
    val date: Instant,
    val result: String
) {
}