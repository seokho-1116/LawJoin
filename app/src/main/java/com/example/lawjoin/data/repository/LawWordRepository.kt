package com.example.lawjoin.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.lawjoin.data.model.LawWord
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LawWordRepository {
    private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("law_words").child("law_word")
    private var lastWordId: String? = null
    private val pageSize = 20

    fun loadInitialLawWords(callback: (List<String>) -> Unit) {
        val query = database.orderByKey().limitToFirst(pageSize)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    lastWordId = dataSnapshot.children.last().key
                    val lawWords = mutableListOf<String>()

                    for (snapshot in dataSnapshot.children) {
                        val word = snapshot.child("용어명").getValue(String::class.java)
                        word?.let { lawWords.add(it) }
                    }

                    callback(lawWords)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error getting law words", databaseError.toException())
            }
        })
    }

    fun loadMoreLawWords(callback: (List<String>) -> Unit) {
        lastWordId?.let {
            val newQuery = database.orderByKey().startAt(it).limitToFirst(pageSize + 1)
            newQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val lawWords = mutableListOf<String>()

                        var isFirst = true
                        for (snapshot in dataSnapshot.children) {
                            if (!isFirst) {
                                val word = snapshot.child("용어명").getValue(String::class.java)
                                word?.let { lawWords.add(it) }
                            } else {
                                isFirst = false
                            }
                        }

                        if (lawWords.isNotEmpty()) {
                            lastWordId = dataSnapshot.children.last().key
                            callback(lawWords)
                        }
                    }
                }

                @SuppressLint("LongLogTag")
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "Error getting more law words", databaseError.toException())
                }
            })
        }
    }

    fun searchLawWords(query: String) : MutableList<String> {
        val lawWords = mutableListOf<String>()
        val searchQuery = database.orderByChild("용어명").startAt(query).endAt(query + "\uf8ff")

        searchQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (snapshot in dataSnapshot.children) {
                    val word = snapshot.child("용어명").getValue(String::class.java)

                    if (word != null) {
                        lawWords.add(word)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error searching law words", databaseError.toException())
            }
        })

        return lawWords
    }

    fun searchLawWord(query: String, callback: (LawWord) -> Unit) {
        val searchQuery = database.orderByChild("용어명").startAt(query).endAt(query + "\uf8ff")

        searchQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val first = dataSnapshot.children.first()
                val wordNum = first.child("용어번호").getValue(Long::class.java)!!.toString()
                val word = first.child("용어명").getValue(String::class.java)!!
                val wordDescription = first.child("설명").getValue(String::class.java)!!

                callback(LawWord("", wordNum, word, wordDescription))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error searching law words", databaseError.toException())
                callback(LawWord())
            }
        })
    }

    fun nextWord(wordNumber: Int, callback: (LawWord) -> Unit) {
        var key = wordNumber
        if (wordNumber == MAX_WORD_LIST_SIZE) {
            key = 0
        }
        val query = database.orderByKey().equalTo(key.toString())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val first = dataSnapshot.children.first()
                val wordNum = first.child("용어번호").getValue(Long::class.java)!!.toString()
                val word = first.child("용어명").getValue(String::class.java)!!
                val wordDescription = first.child("설명").getValue(String::class.java)!!

                callback(LawWord("", wordNum, word, wordDescription))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error searching law words", databaseError.toException())
                callback(LawWord())
            }
        })
    }

    fun previousWord(wordNumber: Int, callback: (LawWord) -> Unit) {
        var key = wordNumber - 2
        if (wordNumber == MIN_WORD_LIST_SIZE) {
            key = MAX_WORD_LIST_SIZE - 1
        }
        val query = database.orderByKey().equalTo(key.toString())

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val first = dataSnapshot.children.first()
                val wordNum = first.child("용어번호").getValue(Long::class.java)!!.toString()
                val word = first.child("용어명").getValue(String::class.java)!!
                val wordDescription = first.child("설명").getValue(String::class.java)!!

                callback(LawWord("", wordNum, word, wordDescription))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error searching law words", databaseError.toException())
                callback(LawWord())
            }
        })
    }

    companion object {
        private const val TAG = "LawWordRepository"
        private const val MAX_WORD_LIST_SIZE = 1023
        private const val MIN_WORD_LIST_SIZE = 1
    }
}
