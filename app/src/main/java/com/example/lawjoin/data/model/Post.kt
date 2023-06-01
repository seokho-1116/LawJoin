package com.example.lawjoin.data.model

import java.io.Serializable
import java.time.ZonedDateTime

class Post(
    val id: Int,
    val title: String,
    val detail: String,
    val ownerId: String,
    val comment: List<Comment>,
    val createTime: ZonedDateTime,
    val modifyTime: ZonedDateTime,
    val isAnonymous: Boolean,
    val isForOnlyLawyer: Boolean,
    val recommendationCount: Int
): Serializable