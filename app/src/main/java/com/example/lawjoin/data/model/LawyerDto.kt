package com.example.lawjoin.data.model

import java.io.Serializable

data class LawyerDto (val uid: String?, val name: String?, val email: String?): Serializable {
    constructor(): this("", "", "")
}