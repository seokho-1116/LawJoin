package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class CounselCase(
    val id: String,
    val title: String,
    val detail: String,
    val date: String,
    val result: String
) {
    constructor(): this("", "", "", ZonedDateTime.now().toString(), "")
}