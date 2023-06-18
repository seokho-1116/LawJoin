package com.example.lawjoin.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lawjoin.data.model.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
class PostRepository {
    private val databaseReference: DatabaseReference = Firebase.database.reference.child("posts")

    companion object{
        private val INSTANCE = PostRepository()

        fun getInstance(): PostRepository {
            return INSTANCE
        }
    }

    fun savePost(callback: (DatabaseReference) -> Task<Void>) {
        callback(databaseReference.child("post").push())
    }

    fun findPost(postId: String, callback: (Post) -> Unit) {
        databaseReference
            .child("post")
            .child(postId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.getValue(Post::class.java)!!
                    callback(post)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun findAllPosts(callback: (List<Post>) -> Unit) {
        databaseReference
            .child("post")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val postList = mutableListOf<Post>()
                    for (data in snapshot.children) {
                        val post = data.getValue(Post::class.java)
                        if (post != null){
                            post.id = data.key.toString()
                            postList.add(post)
                        }
                    }
                    callback(postList)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun saveCounselPost(callback: (DatabaseReference) -> Task<Void>) {
        callback(databaseReference.child("counselPost").push())
    }

    fun findCounselPost(postId: String, callback: (Post) -> Unit) {
        databaseReference
            .child("counselPost")
            .child(postId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val post = snapshot.getValue(Post::class.java)!!
                    callback(post)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun findAllCounselPosts(callback: (List<Post>) -> Unit) {
        databaseReference
            .child("counselPost")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val postList = mutableListOf<Post>()
                    for (data in snapshot.children) {
                        val post = data.getValue(Post::class.java)
                        if (post != null){
                            post.id = data.key.toString()
                            postList.add(post)
                        }
                    }
                    callback(postList)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}