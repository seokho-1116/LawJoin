package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class Lawyer(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profile_url: String = "",
    val office: LawyerOffice = LawyerOffice(
        "", "", ZonedDateTime.now().toString(), ZonedDateTime.now().toString(), ""
    ),
    val career: List<String> = listOf(),
    val counselReviews: List<CounselReview> = listOf(),
    val counselCases: List<CounselCase> = listOf(),
    val introduce: String = "",
    val categories: List<String> = listOf(),
    val certificate: List<String> = listOf(),
    val basicCounselTime: Long = 30L,
    val unavailableTime: List<String> = listOf(),
    val reviewCount: Int = 0,
    val likeCount: Int = 0
//TODO: 성별
) : Serializable{

    fun toData(): CharSequence {
        return "변호사 이름: $name\n법률 사무소 이름: ${office.name}\n" +
                "법률 사무소 전화번호: ${office.phone}\n법률 사무소 위치${office.location}" +
                "법률 사무소 운영시간: ${office.openingTime} ~ ${office.closingTime}"
    }
}