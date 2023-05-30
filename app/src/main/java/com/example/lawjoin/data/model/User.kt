package com.example.lawjoin.data.model

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class User(
    uid: String?, name:String?, email: String?,
    val phone: String?,
    val likeLawyer: List<Lawyer>,
    val enterTime: ZonedDateTime,
    val chatProfile: Uri?
): Person(uid, name, email) {
    constructor(uid: String?, email: String?, name: String?, phone: String?, chatProfile: Uri?): this(uid, name, email,
        phone, emptyList(), ZonedDateTime.now(), chatProfile)
}