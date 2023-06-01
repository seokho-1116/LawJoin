package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class LawyerOffice (
    val name: String,
    val phone: String,
    val openingTime: String,
    val closingTime: String,
    val location: String
    ) {
    constructor(): this(
        "",
        "",
        ZonedDateTime.now().toString(),
        ZonedDateTime.now().plusHours(8L).toString(),
        ""
    )
}
