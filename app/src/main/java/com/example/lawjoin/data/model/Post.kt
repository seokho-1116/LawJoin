package com.example.lawjoin.data.model

import java.time.LocalDateTime

class Post(
    val id: Int,
    val title: String,
    val detail: String,
    val owner: String,
    val createTime: LocalDateTime,
    val modifyTime: LocalDateTime,
    val isAnonymous: Boolean,
    val isForOnlyLawyer: Boolean,
    val recommendationCount: Int
)