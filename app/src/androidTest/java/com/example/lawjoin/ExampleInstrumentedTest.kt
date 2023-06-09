package com.example.lawjoin

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.lawjoin.data.model.CounselCase
import com.example.lawjoin.data.model.CounselReview
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.data.model.LawyerOffice
import com.example.lawjoin.data.model.Message
import com.example.lawjoin.data.repository.ChatRoomRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var chatRoomRepository: ChatRoomRepository = ChatRoomRepository.getInstance()

    @Before
    fun setup() {
        database = Firebase.database
        reference = database.reference
            .child("chat_rooms").child("chat_room")
    }

    @After
    fun cleanup() {
        // Clean up the database after the test
        //reference.removeValue()
    }

    @Test
    fun chatSaveTest() {
        val testDataList = listOf(
            Message("senderUid1", "2023-05-01", "Hello!", true),
            Message("senderUid2", "2023-05-02", "How are you?", false),
            Message("senderUid1", "2023-05-03", "I'm good. Thanks!", true),
            Message("senderUid2", "2023-05-04", "That's great to hear!", true),
            Message("senderUid1", "2023-05-05", "Bye!", false)
        )

        val latch = CountDownLatch(1)

        reference.child("JSoAJdMcaffIKhMzzVVGMqErQv33")
            .child("BOT")
            .child("messages")
            .setValue(testDataList)
            .addOnSuccessListener {
                latch.countDown()
            }
        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for onDataChange callback")
        }
    }

    @Test
    fun chatWithsaveTest() {
        val testDataList = listOf(
            Message("senderUid1", "2023-05-01", "Hello!", true),
            Message("senderUid2", "2023-05-02", "How are you?", false),
            Message("senderUid1", "2023-05-03", "I'm good. Thanks!", true),
            Message("senderUid2", "2023-05-04", "That's great to hear!", true),
            Message("senderUid1", "2023-05-05", "Bye!", false)
        )

        val userUid = "JSoAJdMcaffIKhMzzVVGMqErQv33" // Replace with the actual user UID

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("chat_rooms/chat_room/$userUid/BOT/messages")

        val latch = CountDownLatch(1)

        val dataMap = mutableMapOf<String, Message>()
        for (message in testDataList) {
            val newMessageRef = reference.push()
            val messageId = newMessageRef.key
            if (messageId != null) {
                dataMap[messageId] = message
            }
        }

        reference.updateChildren(dataMap as Map<String, Any>)
            .addOnSuccessListener {
                latch.countDown()
            }
            .addOnFailureListener { error ->
                fail("Failed to add messages: ${error.message}")
            }

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for messages to be added")
        }
    }

    @Test
    fun chatsSaveTest() {
        val testDataList = listOf(
            Message("senderUid1", "2023-05-01", "Hello!", true),
            Message("senderUid2", "2023-05-02", "How are you?", false),
            Message("senderUid1", "2023-05-03", "I'm good. Thanks!", true),
            Message("senderUid2", "2023-05-04", "That's great to hear!", true),
            Message("senderUid1", "2023-05-05", "Bye!", false)
        )

        val userUid = "JSoAJdMcaffIKhMzzVVGMqErQv33" // Replace with the actual user UID

        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("chat_rooms/chat_room/$userUid/BOT/messages")

        val latch = CountDownLatch(1)

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentList = dataSnapshot.getValue(object : GenericTypeIndicator<List<Message>>() {})
                val updatedList = currentList?.toMutableList()?.apply {
                    addAll(testDataList)
                } ?: testDataList

                reference.push().setValue(updatedList)
                    .addOnSuccessListener {
                        latch.countDown()
                    }
                    .addOnFailureListener { error ->
                        fail("Failed to update list: ${error.message}")
                    }
            }

            override fun onCancelled(error: DatabaseError) {
                fail("Database error occurred: ${error.message}")
            }
        })

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for onDataChange callback")
        }
    }


    @Test
    fun chatRoomTest() {

        val latch = CountDownLatch(1)

        /*chatRoomRepository.findUserChatRoomByKey("JSoAJdMcaffIKhMzzVVGMqErQv33", "BOT") {
            it.child("messages").children.las
            Log.d("test data", messages.keys.toString())
            Log.d("test data", messages.values.toString())
            Log.d("result", messages.toString())
            latch.countDown()
        }*/

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for onDataChange callback")
        }
    }

    @Test
    fun saveLawyerTest() {
        val lawyer = Lawyer(
            uid = "123456789",
            name = "John Doe",
            email = "johndoe@example.com",
            profile_url = "https://example.com/profile",
            office = LawyerOffice(
                name = "Law Firm",
                phone = "123-456-7890",
                openingTime = "09:00",
                closingTime = "17:00",
                location = "123 Main St, City"
            ),
            career = listOf("Senior Lawyer", "10+ years of experience"),
            counselReviews = listOf(
                CounselReview(
                    title = "Great Service",
                    detail = "I highly recommend this lawyer. Very knowledgeable and professional.",
                    createdTime = ZonedDateTime.now(ZoneId.of("UTC")).toString(),
                    category = "형법",
                    writerId = "user123",
                    lawyerId = "123456789"
                ),
                CounselReview(
                    title = "Excellent Work",
                    detail = "The lawyer handled my case with utmost care and achieved a favorable outcome.",
                    createdTime = ZonedDateTime.now(ZoneId.of("UTC")).toString(),
                    category = "민법",
                    writerId = "user456",
                    lawyerId = "123456789"
                )
            ),
            counselCases = listOf(
                CounselCase(
                    id = "case123",
                    title = "Personal Injury Case",
                    detail = "Represented client in a personal injury lawsuit against a negligent driver.",
                    date = ZonedDateTime.now(ZoneId.of("UTC")).toString(),
                    result = "Settlement reached"
                ),
                CounselCase(
                    id = "case456",
                    title = "Contract Dispute",
                    detail = "Assisted client in resolving a contract dispute with a business partner.",
                    date = ZonedDateTime.now(ZoneId.of("UTC")).toString(),
                    result = "Case pending"
                )
            ),
            introduce = "I specialize in personal injury cases and contract law.",
            categories = listOf("Personal Injury", "Contract Law"),
            certificate = listOf("Bar Association Certification", "Specialized Training in Personal Injury Law"),
            basicCounselTime = 60L,
            unavailableTime = listOf(ZonedDateTime.now(ZoneId.of("UTC")).toString(), ZonedDateTime.now(ZoneId.of("UTC")).plusDays(9).toString() ),
            reviewCount = 2,
            likeCount = 10
        )

        database.reference.child("lawyers").child("lawyer").child(lawyer.uid)
            .setValue(lawyer)

        val latch = CountDownLatch(1)
        database.reference.child("lawyers").child("lawyer")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(
                        "FirebaseQuery",
                        "Query canceled or encountered an error: ${error.message}"
                    )
                    latch.countDown() // Make sure to release the latch in case of cancellation or error
                }
            })
        if (!latch.await(5, TimeUnit.SECONDS)) {}
    }
}