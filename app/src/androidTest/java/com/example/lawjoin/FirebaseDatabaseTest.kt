package com.example.lawjoin

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.Test
import org.junit.runner.RunWith
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class FirebaseDatabaseTest {

    @Test
    fun chatBotTest() {
        val question = "부당해고"

        val collectionPath = "legal_knowledges"

        var result = 0
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection(collectionPath)

        val query = collectionRef.orderBy("question")
            .startAt(question)
            .endAt(question + "\uf8ff")


        val latch = CountDownLatch(1)
        query.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                for (documentSnapshot in querySnapshot) {
                    val answer = documentSnapshot.getString("answer")
                    Log.d("test result", answer.toString())
                    result += 1
                }
                latch.countDown()
            }
        }

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw TimeoutException()
        }
        Log.d("test result", result.toString())

    }

    @Test
    fun saveTest() {
        val latch = CountDownLatch(1)
        val docData = hashMapOf(
            "userId" to "외않",
            "lawyerId" to "대 병학",
            "startTime" to "7월 17일",
            "summary" to "하이 잘됏나요?"
        )
        Firebase.database.getReference("reservations").child("reservation").child("1234").setValue(docData)
            .addOnSuccessListener {
                latch.countDown()
            }

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw TimeoutException()
        }
    }
    @Test
    fun deleteTest(){
        val latch = CountDownLatch(1)
        val reservationsRef = Firebase.database.getReference("reservations")
            .child("reservation").child("1234")

        reservationsRef.child("lawyerId").setValue(null)
            .addOnSuccessListener {
                latch.countDown()
            }
        reservationsRef.child("summary").setValue(null)
            .addOnSuccessListener {
                latch.countDown()
            }

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw TimeoutException()
        }
    }
}