package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class Comment (
    val id: String,
    val title: String,
    val detail: String,
    val owner: String,
    val createTime: String,
    val modifyTime: String
): Serializable {
    constructor(): this(
        "",
        "",
        "",
        "",
        ZonedDateTime.now().toString(),
        ZonedDateTime.now().plusHours(8L).toString()
    )
}