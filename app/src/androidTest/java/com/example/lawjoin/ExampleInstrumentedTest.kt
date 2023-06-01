package com.example.lawjoin

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.lawjoin.data.model.CounselCase
import com.example.lawjoin.data.model.CounselReview
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.data.model.LawyerOffice
import com.example.lawjoin.data.model.Message
import com.example.lawjoin.data.repository.ChatRoomRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
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
            uid = "BOT",
            name = "Jane Smith",
            email = "janesmith@example.com",
            profile_url = "https://example.com/profile.jpg",
            office = LawyerOffice(
                name = "Law Office ABC",
                phone = "987-654-3210",
                openingTime = "2023-05-28T08:30:00+09:00",
                closingTime = "2023-05-28T17:30:00+09:00",
                location = "456 Elm St, City, Country"
            ),
            career = listOf(
                "Associate Attorney at Law Firm XYZ (2015-2018)",
                "Founding Partner at Law Office ABC (2018-present)"
            ),
            counselReviews = listOf(
                CounselReview(
                    title = "Excellent Lawyer",
                    detail = "Jane provided exceptional legal guidance and resolved my case effectively.",
                    createdTime = "2023-05-27T11:45:00+09:00",
                    writerId = "4",
                    lawyerId = "2"
                ),
                CounselReview(
                    title = "Highly Knowledgeable",
                    detail = "I'm impressed with Jane's expertise and professionalism. Highly recommended.",
                    createdTime = "2023-05-26T16:15:00+09:00",
                    writerId = "5",
                    lawyerId = "2"
                )
            ),
            counselCases = listOf(
                CounselCase(
                    id = "3",
                    title = "Personal Injury Claim",
                    detail = "Successfully represented a client in a personal injury lawsuit.",
                    date = "2023-05-25T11:00:00+09:00",
                    result = "Settlement reached"
                ),
                CounselCase(
                    id = "4",
                    title = "Business Contract Review",
                    detail = "Provided legal advice for reviewing a complex business contract.",
                    date = "2023-05-24T13:30:00+09:00",
                    result = "Contract renegotiated"
                )
            ),
            introduce = "I specialize in personal injury law and business contracts. Contact me for expert legal assistance.",
            categories = listOf("Personal Injury Law", "Contract Law"),
            certificate = listOf("State Bar Certification"),
            basicCounselTime = 45L, // 45 minutes
            unavailableTime = listOf(
                "2023-05-29T09:30:00+09:00",
                "2023-05-30T14:00:00+09:00"
            ),
            reviewCount = 2,
            likeCount = 8
        )

        database.reference.child("lawyers").child("lawyer").child(lawyer.uid)
            .setValue(lawyer)

        val latch = CountDownLatch(1)
        val list = mutableListOf<Lawyer>()
        database.reference.child("lawyers").child("lawyer")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.add(snapshot.child(lawyer.uid).getValue(Lawyer::class.java)!!)
                    latch.countDown()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(
                        "FirebaseQuery",
                        "Query canceled or encountered an error: ${error.message}"
                    )
                    latch.countDown() // Make sure to release the latch in case of cancellation or error
                }
            })
        // Wait for the latch countdown with a timeout
        if (!latch.await(5, TimeUnit.SECONDS)) {

            /*val latch = CountDownLatch(1)
        database.reference.child("lawyers").child("lawyer").child("1")
            .child("unavailableTime").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val itemCount = dataSnapshot.childrenCount.toInt()
                // Add the new item using the count as the index
                val newItemValue = "time4"
                dataSnapshot.ref.child(itemCount.toString()).setValue(newItemValue)
                latch.countDown()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error case if necessary
            }
        })

        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for onDataChange callback")
        }*/
        }
    }

    @Test
    fun testReadData1() {
        data class Pest(var receiver: String, var messages: MutableList<String>) {
            constructor() : this("", mutableListOf())
        }

        val latch = CountDownLatch(1) // Create a CountDownLatch with an initial count of 1
        var value: Pest?

        val testobj = Pest("seokho", mutableListOf("1", "2", "3"))

        reference.child("uid").setValue(testobj)

        // Read the data from the database
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.first() {
                    it.child("receiver").value == "seokho"
                }.child("messages")
                val list = mutableListOf<String>()
                for (i in data.children) {
                    list.add(i.getValue<String>()!!)
                }
                Log.e("FirebaseQuery", list[0])
                assertEquals("1", list[0])
                latch.countDown() // Decrement the latch count to indicate that onDataChange has finished
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(
                    "FirebaseQuery",
                    "Query canceled or encountered an error: ${error.message}"
                )
                latch.countDown() // Make sure to release the latch in case of cancellation or error
            }
        })
        // Wait for the latch countdown with a timeout
        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for onDataChange callback")
        }

        // Proceed with further assertions or actions after the callback has completed
    }

    @Test
    fun testReadData() {
        val latch = CountDownLatch(1) // Create a CountDownLatch with an initial count of 1
        var value: String? = null


        // Read the data from the database
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value = snapshot.getValue(String::class.java).toString()
                Log.e("FirebaseQuery", value.toString())
                assertEquals("Hello, World!", value)
                latch.countDown() // Decrement the latch count to indicate that onDataChange has finished
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(
                    "FirebaseQuery",
                    "Query canceled or encountered an error: ${error.message}"
                )
                latch.countDown() // Make sure to release the latch in case of cancellation or error
            }
        })

        // Wait for the latch countdown with a timeout
        if (!latch.await(5, TimeUnit.SECONDS)) {
            fail("Timeout waiting for onDataChange callback")
        }

        // Proceed with further assertions or actions after the callback has completed
        print(value)
    }
}