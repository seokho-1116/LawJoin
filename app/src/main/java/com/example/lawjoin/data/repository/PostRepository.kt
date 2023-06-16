package com.example.lawjoin.data.repository

import android.util.Log
import com.example.lawjoin.data.model.Comment
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

    fun findPost(postId: String, callback: (DataSnapshot) -> Unit) {
        database.child("post")
            .child(postId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("CHAT DATA", "failed to get chat rooms data")
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot)
                }
            })
    }

    fun updatePostComment(postId: String, comment: Comment) {
        database.child("post")
            .child(postId)
            .child("comments")
            .push()
            .setValue(comment)
    }

    fun updatePostRecommendCount(postId: String) {
        val reference = database.child("post")
            .child(postId)
            .child("recommendationCount")

        reference
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val previousValue = dataSnapshot.getValue(Int::class.java)
                    val updatedValue = previousValue?.plus(1) ?: 1
                    reference.setValue(updatedValue)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}