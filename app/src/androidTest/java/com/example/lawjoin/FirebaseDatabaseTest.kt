package com.example.lawjoin

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.junit.Test
import org.junit.runner.RunWith
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
}