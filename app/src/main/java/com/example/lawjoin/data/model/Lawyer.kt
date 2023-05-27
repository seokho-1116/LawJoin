package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class Lawyer (
    val id: Int,
    val name: String,
    val profile_url: String,
    val office: LawyerOffice,
    val career: List<String>,
    val counselReviews: List<CounselReview>,
    val counselCases: List<CounselCase>,
    val introduce: String,
    val categories: List<String>,
    val certificate: List<String>,
    val basicCounselTime: Long,
    val unavailableTime: List<ZonedDateTime>,
    val reviewCount: Int,
    val likeCount: Int
) : Serializable {
    fun toData(): CharSequence? {
        TODO("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(): this(1, "test", "none",
        LawyerOffice("test-office", "010-1234-1234",
            ZonedDateTime.now(ZoneId.of("UTC")), ZonedDateTime.now(ZoneId.of("UTC")).plusMinutes(540L), "seoul"),
    listOf(), listOf(), listOf(), "hello i'm test", listOf(), listOf(), 30L,
        listOf(ZonedDateTime.of(LocalDate.now(), LocalTime.of(6, 30), ZoneId.of("UTC"))), 30, 30)
}