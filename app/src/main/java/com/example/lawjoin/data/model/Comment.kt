package com.example.lawjoin.data.model

import java.time.LocalDateTime

class Comment (
    val id: Int,
    val detail: String,
    val owner: String,
    val createTime: LocalDateTime,
    val modifyTime: LocalDateTime
)