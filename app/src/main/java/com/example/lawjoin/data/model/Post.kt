package com.example.lawjoin.data.model

import java.time.ZonedDateTime

class Post(
    val id: Int,
    val title: String,
    val detail: String,
    val owner: String,
    val createTime: ZonedDateTime,
    val modifyTime: ZonedDateTime,
    val isAnonymous: Boolean,
    val isForOnlyLawyer: Boolean,
    val recommendationCount: Int
)