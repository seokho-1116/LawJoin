package com.example.lawjoin.data.model

import java.io.Serializable

data class LawWord(
    val wordId: String = "",
    val wordNumber: String = "",
    val word: String = "",
    val wordDescription: String = ""
) : Serializable {
    constructor(word: String) : this("", "", word, "")
}
