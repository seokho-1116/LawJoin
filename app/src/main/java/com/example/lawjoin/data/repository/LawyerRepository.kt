package com.example.lawjoin.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lawjoin.data.model.CounselCase
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.data.model.LawyerOffice
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class LawyerRepository {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun findLawyerById(id: String): Lawyer? {
        return db.collection("lawyers")
            .document(id)
            .get()
            .await()
            .toObject(Lawyer::class.java)
    }

    suspend fun findAllLawyers(): List<Lawyer> {
        return db.collection("lawyers")
            .get()
            .await()
            .toObjects(Lawyer::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun findLawyerById(lawyerId: Int) : Lawyer {
        return Lawyer(
            1,
            "seokho",
            "hello",
            LawyerOffice("한공", "010-1234-1234", ZonedDateTime.now(ZoneId.of("UTC")), ZonedDateTime.now(
                ZoneId.of("UTC")), "seoul"),
            listOf("1", "2", "3"),
            listOf(),
            listOf(CounselCase(1, "hos", "hoho", ZonedDateTime.now(ZoneId.of("UTC")), "succeed")),
            "hello my name is seokho",
            listOf(),
            listOf(),
            30, listOf(), 50, 60)
    }

    fun updateUnavailableTime(lawyer: Lawyer) {
        val lawyerCollection = db.collection("lawyers")
        val lawyerDocument = lawyerCollection.document(lawyer.id.toString())

        lawyerDocument.set(lawyer)
    }
}