package com.example.lawjoin.data.model

import java.time.LocalDateTime

class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val likeLawyer: List<Lawyer>,
    val enterTime: LocalDateTime
) {
}