package com.example.lawjoin.data.model

import java.io.Serializable

data class AuthUserDto(
    var uid: String? = "",
    var name:String? = "",
    var email: String? = "",
    var chatProfile: String? = ""
): Serializable