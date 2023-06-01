package com.example.lawjoin.data.model

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class User(
    val uid: String?, val name:String?, val email: String?,
    val phone: String?,
    val likeLawyer: List<String>,
    val recommendPost: List<String>,
    val likeComment: List<String>,
    val bookmarkedPost: List<String>,
    val enterTime: String,
    val chatProfile: String?
): Serializable {
    constructor() : this(
        null,
        null,
        null,
        null,
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        "",
        null
    )

    constructor(uid: String?, email: String?, name: String?, phone: String?, chatProfile: String?) :
            this(uid, name, email, phone, listOf(), listOf(), listOf(), listOf(), ZonedDateTime.now().toString(), chatProfile)
}