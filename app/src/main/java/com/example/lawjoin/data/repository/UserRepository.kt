package com.example.lawjoin.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserRepository private constructor() {
    private val databaseReference: DatabaseReference = Firebase.database.reference.child("users")

    companion object{
        private val INSTANCE = UserRepository()

        fun getInstance(): UserRepository {
            return INSTANCE
        }
    }

    fun saveUser(uid: String, callback: (DatabaseReference) -> Task<Void>) {
        callback(databaseReference.child("user").child(uid))
    }

    fun findUser(uid: String, callback: (DataSnapshot) -> Unit) {
        databaseReference
            .child("user")
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun updateBookmark(uid: String, postId: String) {
        val bookmarkRef = databaseReference
            .child("user")
            .child(uid)
            .child("bookmarked_posts")

        bookmarkRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val exists = dataSnapshot.hasChild(postId)
                if (!exists) {
                    bookmarkRef.child(postId).setValue(true)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    fun updateRecommendedPost(uid: String, postId: String) {
        val recommendedRef = databaseReference
            .child("user")
            .child(uid)
            .child("recommended_posts")

        recommendedRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val exists = dataSnapshot.hasChild(postId)
                if (!exists) {
                    recommendedRef.child(postId).setValue(true)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    fun updateLikeLawyer(uid: String, lawyerId: String) {
        val likeLawyerRef = databaseReference
            .child("user")
            .child(uid)
            .child("like_lawyers")

        likeLawyerRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val exists = dataSnapshot.hasChild(lawyerId)
                if (!exists) {
                    likeLawyerRef.child(lawyerId).setValue(true)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    fun deleteBookmark(uid: String, postId: String) {
        val bookmarkRef = databaseReference
            .child("user")
            .child(uid)
            .child("bookmarked_posts")
            .child(postId)

        bookmarkRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    bookmarkRef.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Error handling
            }
        })
    }

    fun deleteRecommend(uid: String, postId: String) {
        val recommendRef = databaseReference
            .child("user")
            .child(uid)
            .child("recommended_posts")
            .child(postId)

        recommendRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    recommendRef.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Error handling
            }
        })
    }

    fun deleteLikeLawyer(uid: String, lawyerId: String) {
        val likeLawyersRef = databaseReference
            .child("user")
            .child(uid)
            .child("like_lawyers")
            .child(lawyerId)

        likeLawyersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    likeLawyersRef.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}