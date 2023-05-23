package com.example.lawjoin.data.model

import android.icu.util.DateInterval

class Lawyer(
    val id: Int,
    val name: String,
    val profile_url: String,
    val office: LawyerOffice,
    val career: List<String>,
    val counselCases: List<CounselCase>,
    val introduce: String,
    val categories: List<String>,
    val certificate: List<String>,
    val basicCounselTime: Int,
    val reviewCount: Int,
    val likeCount: Int
) {
}