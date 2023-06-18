package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.ZonedDateTime


@RequiresApi(Build.VERSION_CODES.O)
class Post(
    var id: String = "",
    val title: String,
    val detail: String,
    val ownerId: String,
    val comment: Comment = Comment(
        "", "", "", "", "", ""
    ),
    val createTime: String,
    val modifyTime: String,
    val isAnonymous: Boolean,
    val isForOnlyLawyer: Boolean,
    val recommendationCount: Int
) : Serializable {
    constructor(): this(
        "",
        "",
        "",
        "",
        Comment(
            "", "", "", "", "", ""
        ),
        ZonedDateTime.now().toString(),
        ZonedDateTime.now().plusHours(8L).toString(),
        false,
        false,
        0

    )
}