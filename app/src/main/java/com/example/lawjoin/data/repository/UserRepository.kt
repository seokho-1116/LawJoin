package com.example.lawjoin.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserRepository private constructor() {
    private val databaseReference: DatabaseReference = Firebase.database.reference.child("users")
    private val auth = Firebase.auth

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
}