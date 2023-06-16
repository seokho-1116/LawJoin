package com.example.lawjoin.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.lawjoin.data.model.CounselReview
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.data.model.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
class LawyerRepository private constructor() {

    private val databaseReference: DatabaseReference = Firebase.database.reference.child("lawyers")

    companion object{
        private val INSTANCE = LawyerRepository()

        fun getInstance(): LawyerRepository {
            return INSTANCE
        }
    }

    fun findLawyerById(uid: String, callback: (Lawyer) -> Unit) {
        databaseReference
            .child("lawyer")
            .child(uid)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lawyer = snapshot.getValue(Lawyer::class.java)!!
                    for (case in snapshot.child("counselCases").children) {
                        lawyer.counselCaseList.add(case.getValue(Post::class.java)!!)
                    }
                    for (reviews in snapshot.child("counselReviews").children) {
                        lawyer.counselReviewList.add(reviews.getValue(CounselReview::class.java)!!)
                    }
                    callback(lawyer)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseQuery", "Query canceled or encountered an error: ${error.message}")
                }
            })
    }

    fun findAllLawyers(callback: (List<Lawyer>) -> Unit){
        databaseReference
            .child("lawyer")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lawyerList = mutableListOf<Lawyer>()
                    for (data in snapshot.children) {
                        val lawyer = data.getValue(Lawyer::class.java)
                        for (case in data.child("counselCases").children) {
                            lawyer!!.counselCaseList.add(case.getValue(Post::class.java)!!)
                        }
                        for (reviews in data.child("counselReviews").children) {
                            lawyer!!.counselReviewList.add(reviews.getValue(CounselReview::class.java)!!)
                        }
                        if (lawyer != null) {
                            lawyerList.add(lawyer)
                        }
                    }
                    callback(lawyerList)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseQuery", "Query canceled or encountered an error: ${error.message}")
                }
            })
    }

    fun saveLawyer(uid: String, callback: (DatabaseReference) -> Task<Void>) {
        callback(databaseReference.child("lawyer").child(uid))
    }

    fun updateUnavailableTime(id: String, time: String) {
        databaseReference
            .child("lawyer")
            .child(id)
            .child("unavailableTime").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val itemCount = dataSnapshot.childrenCount.toInt()
                    dataSnapshot.ref
                        .child(itemCount.toString())
                        .setValue(time)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }

    fun saveLawyerReview(lawyerId: String, counselReview: CounselReview) {
        databaseReference
            .child("lawyer")
            .child(lawyerId)
            .child("counselReviews")
            .push().setValue(counselReview)
    }
}