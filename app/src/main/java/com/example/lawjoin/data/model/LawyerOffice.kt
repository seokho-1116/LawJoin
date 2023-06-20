package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.ZoneId
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class LawyerOffice (
    val name: String = "",
    val phone: String = "",
    val openingTime: String = ZonedDateTime.now(ZoneId.of("UTC")).toString(),
    val closingTime: String = ZonedDateTime.now(ZoneId.of("UTC")).plusHours(8L).toString(),
    val location: String = ""
    ) : Serializable
