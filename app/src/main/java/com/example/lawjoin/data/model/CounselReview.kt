package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.ZoneId
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class CounselReview(
    val title: String = "",
    val detail: String = "",
    val createdTime: String = ZonedDateTime.now(ZoneId.of("UTC")).toString(),
    val writerId: String = "",
    val category: String = "",
    val lawyerId: String =""
) : Serializable
