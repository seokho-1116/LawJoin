package com.example.lawjoin.data.model

import java.io.Serializable


class Comment (
    val detail: String = "",
    val owner: String = "",
    val createTime: String = "",
    val modifyTime: String = ""
): Serializable