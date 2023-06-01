package com.example.lawjoin.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class CounselReview(
    val title: String,
    val detail: String,
    val createdTime: String,
    val writerId: String?,
    val lawyerId: String?
) {
    constructor() : this("", "", ZonedDateTime.now().toString(), null, null)
}
