package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.ZoneId
import java.time.ZonedDateTime


@RequiresApi(Build.VERSION_CODES.O)
data class CounselReservation(
    val startTime: String = ZonedDateTime.now(ZoneId.of("UTC")).toString(),
    val userId: String = "",
    val lawyerId: String = "",
    val summary: String = "",
    val userName: String = ""
) : Serializable