package com.example.lawjoin.data.repository

import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatBotRepository private constructor() {
    private val database = Firebase.firestore

    companion object{
        private val INSTANCE = ChatBotRepository()

        fun getInstance(): ChatBotRepository {
            return INSTANCE
        }
    }

    fun findAnswerByQuestion(question: String, callback: (QuerySnapshot) -> Unit) {
        val query = database
            .collection("legal_knowledges")
            .whereGreaterThanOrEqualTo("question", question)
            .whereLessThanOrEqualTo("question", question + "\uf8ff")

        query.get().addOnSuccessListener { querySnapshot ->
            callback(querySnapshot)
        }
    }
}

