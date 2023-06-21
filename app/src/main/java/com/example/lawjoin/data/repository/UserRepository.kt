package com.example.lawjoin.data.repository

import android.util.Log
import com.example.lawjoin.data.model.Post
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
        private const val TAG = "UserRepository"

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
                    Log.e(TAG, "Query canceled or encountered an error: ${error.message}")
                }
            })
    }

    fun updateBookmark(uid: String, post: Post) {
        val bookmarkRef = databaseReference
            .child("user")
            .child(uid)
            .child("bookmarked_posts")

        bookmarkRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val exists = dataSnapshot.hasChild(post.id)
                if (!exists) {
                    bookmarkRef.child(post.id).setValue(post)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Query canceled or encountered an error: ${databaseError.message}")
            }
        })
    }

    fun updateRecommendedPost(uid: String, post: Post) {
        val recommendedRef = databaseReference
            .child("user")
            .child(uid)
            .child("recommended_posts")

        recommendedRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val exists = dataSnapshot.hasChild(post.id)
                if (!exists) {
                    recommendedRef.child(post.id).setValue(post)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Query canceled or encountered an error: ${databaseError.message}")
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
                Log.e(TAG, "Query canceled or encountered an error: ${databaseError.message}")
            }
        })
    }

    fun deleteBookmark(uid: String, post: Post) {
        val bookmarkRef = databaseReference
            .child("user")
            .child(uid)
            .child("bookmarked_posts")
            .child(post.id)

        bookmarkRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    bookmarkRef.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Query canceled or encountered an error: ${databaseError.message}")
            }
        })
    }

    fun deleteRecommend(uid: String, post: Post) {
        val recommendRef = databaseReference
            .child("user")
            .child(uid)
            .child("recommended_posts")
            .child(post.id)

        recommendRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    recommendRef.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Query canceled or encountered an error: ${databaseError.message}")
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
                Log.e(TAG, "Query canceled or encountered an error: ${databaseError.message}")
            }
        })
    }

    fun findLikeLawyerKey(uid: String, callback: (List<String>) -> Unit) {
        val likeLawyersRef = databaseReference
            .child("user")
            .child(uid)
            .child("like_lawyers")

        likeLawyersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val keys = mutableListOf<String>()
                for (lawyerId in dataSnapshot.children) {
                    keys.add(lawyerId.key.toString())
                }
                callback(keys)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Query canceled or encountered an error: ${databaseError.message}")
            }
        })
    }

    fun findRecommendedPost(uid: String, callback: (List<String>) -> Unit) {
        val likeLawyersRef = databaseReference
            .child("user")
            .child(uid)
            .child("recommended_posts")

        likeLawyersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val keys = mutableListOf<String>()
                for (postId in dataSnapshot.children) {
                    keys.add(postId.key.toString())
                }
                callback(keys)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Query canceled or encountered an error: ${databaseError.message}")
            }
        })
    }

    fun findBookmarkedPost(uid: String, callback: (List<String>) -> Unit) {
        val likeLawyersRef = databaseReference
            .child("user")
            .child(uid)
            .child("bookmarked_posts")

        likeLawyersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val keys = mutableListOf<String>()
                for (postId in dataSnapshot.children) {
                    keys.add(postId.key.toString())
                }
                callback(keys)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Query canceled or encountered an error: ${databaseError.message}")
            }
        })
    }
}