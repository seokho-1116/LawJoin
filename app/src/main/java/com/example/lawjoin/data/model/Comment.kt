package com.example.lawjoin.data.model

import java.time.Instant

class Comment (
    val id: Int,
    val detail: String,
    val owner: String,
    val createTime: Instant,
    val modifyTime: Instant
)