package com.example.lawjoin.data.model

import java.io.Serializable
import java.time.ZonedDateTime

data class Message(
    var senderUid: String = "",
    var sendDate: ZonedDateTime,
    var content: String = "",
    var confirmed:Boolean=false
) : Serializable {
}