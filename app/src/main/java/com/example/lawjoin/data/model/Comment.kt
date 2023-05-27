package com.example.lawjoin.data.model

import java.time.ZonedDateTime

class Comment (
    val id: Int,
    val detail: String,
    val owner: String,
    val createTime: ZonedDateTime,
    val modifyTime: ZonedDateTime
)