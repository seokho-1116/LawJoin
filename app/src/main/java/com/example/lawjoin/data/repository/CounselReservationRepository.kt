package com.example.lawjoin.data.repository

import android.util.Log
import com.example.lawjoin.data.model.CounselReservation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CounselReservationRepository private constructor() {
    private val db: FirebaseDatabase = Firebase.database

    companion object{
        private val INSTANCE = CounselReservationRepository()

        fun getInstance(): CounselReservationRepository {
            return INSTANCE
        }
    }

    fun existCounselReservation(uid: String, callback: (Boolean) -> Unit) {
        db.getReference("reservations")
            .child("reservation")
            .child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot.exists())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("repository", "data change error");
                }
            })
    }

    fun saveCounselReservation(counselReservation: CounselReservation) {
        db.getReference("reservations")
            .child("reservation")
            .child(counselReservation.userId.toString())
            .setValue(counselReservation)
    }
}
