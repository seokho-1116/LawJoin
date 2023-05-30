package com.example.lawjoin.data.model

import java.io.Serializable

abstract class Person (
    val uid: String?,
    val name:String?,
    val email: String?): Serializable {

}