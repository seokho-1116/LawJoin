package com.example.lawjoin.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.lawjoin.data.model.Lawyer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
class LawyerRepository {

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
            .equalTo(uid, "uid")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.children.firstOrNull()
                    val lawyer = data?.getValue(Lawyer::class.java)!!
                    callback(lawyer)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseQuery", "Query canceled or encountered an error: ${error.message}")
                }
            })
    }

    fun getAllLawyers(callback: (List<Lawyer>) -> Unit){
        databaseReference
            .child("lawyer")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lawyerList = mutableListOf<Lawyer>()
                    for (data in snapshot.children) {
                        val lawyer = data.getValue(Lawyer::class.java)
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

    fun updateUnavailableTime(lawyer: Lawyer) {
/*        val lawyerCollection = databaseReference.collection("lawyers")
        val lawyerDocument = lawyerCollection.document(lawyer.id.toString())

        lawyerDocument.set(lawyer)*/
    }
}