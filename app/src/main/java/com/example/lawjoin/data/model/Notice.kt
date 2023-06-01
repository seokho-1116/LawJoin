package com.example.lawjoin.data.model

import java.util.Date

class Notice(
    val id: Int,
    val title: String,
    val detail: String,
    val owner: String,
    val createTime: Date,
    val modifyTime: Date,
    val isAnonymous: Boolean,
    val isForOnlyLawyer: Boolean,
    val recommendationCount: Int
)