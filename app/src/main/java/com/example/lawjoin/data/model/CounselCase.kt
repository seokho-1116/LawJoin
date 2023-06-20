package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.ZoneId
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class CounselCase(
    val id: String = "",
    val title: String = "",
    val detail: String = "",
    val date: String = ZonedDateTime.now(ZoneId.of("UTC")).toString(),
    val result: String
) : Serializable