package com.example.lawjoin.data.model

import java.io.Serializable


class Comment (
    val id: String,
    val title: String,
    val detail: String,
    val owner: String,
    val createTime: String,
    val modifyTime: String
): Serializable