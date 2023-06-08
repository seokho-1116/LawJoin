package com.example.lawjoin.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostRepository private constructor() {
    private val database = Firebase.database.reference.child("posts")

    companion object{
        private val INSTANCE = PostRepository()

        fun getInstance(): PostRepository {
            return INSTANCE
        }
    }

    fun savePost(callback: (DatabaseReference) -> Unit) {
        callback(database)
    }

    fun findAllPost(callback: (DataSnapshot) -> Boolean) {
        database.child("post")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}