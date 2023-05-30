package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class Lawyer(uid: String, name: String, email: String,
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
             val likeCount: Int): Person(uid, name, email) {

    fun toData(): CharSequence {
        return "변호사 이름: $name\n법률 사무소 이름: ${office.name}\n" +
                "법률 사무소 전화번호: ${office.phone}\n법률 사무소 위치${office.location}" +
                "법률 사무소 운영시간: ${office.openingTime.toLocalTime()} ~ ${office.closingTime.toLocalTime()}"
    }
}