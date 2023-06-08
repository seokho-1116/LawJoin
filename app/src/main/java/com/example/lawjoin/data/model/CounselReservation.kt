package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.ZonedDateTime


@RequiresApi(Build.VERSION_CODES.O)
class CounselReservation(
    val startTime: String,
    val userId: String?,
    val lawyerId: String?,
    val summary: String
) : Serializable {
    constructor() : this(ZonedDateTime.now().toString(), null, null, "")
}