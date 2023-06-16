package com.example.lawjoin.data.model

import java.io.Serializable
import java.time.ZonedDateTime

class Post(
    var id: String = "",
    val title: String = "",
    val detail: String = "",
    val ownerId: String = "",
    val commentList: MutableList<Comment> = mutableListOf(),
    val createTime: String = "",
    val modifyTime: String = "",
    val isAnonymous: Boolean = false,
    val isForOnlyLawyer: Boolean = false,
    val recommendationCount: Int = 0
): Serializable