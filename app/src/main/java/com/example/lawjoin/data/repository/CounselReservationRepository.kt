package com.example.lawjoin.data.repository

import com.example.lawjoin.data.model.CounselReservation
import com.google.firebase.firestore.FirebaseFirestore

class CounselReservationRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun saveCounselReservation(counselReservation: CounselReservation) {
        db.collection("counsel_reservations")
            .add(counselReservation)
    }
}
