package com.example.lawjoin.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lawjoin.data.model.CounselCase
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.data.model.LawyerOffice
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.Instant

class LawyerRepository {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private suspend fun getTokenResult(id: String): Lawyer? {
        return db.collection("lawyers")
            .document(id)
            .get()
            .await()
            .toObject(Lawyer::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun findLawyerById(lawyerId: Int) : Lawyer {
        return Lawyer(
            1,
            "seokho",
            "hello",
            LawyerOffice("한공", "010-1234-1234", "07:00 - 19:00", "seoul"),
            listOf("1", "2", "3"),
            listOf(),
            listOf(CounselCase(1, "hos", "hoho", Instant.now(), "succeed")),
            "hello my name is seokho",
            listOf(),
            listOf(),
            30, 40, 50)
    }
}