package com.example.lawjoin.data.repository

import com.example.lawjoin.data.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.tasks.await

class PostRepository {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()



    suspend fun findAllPosts(): List<Post> {
        return db.collection("posts")
            .get()
            .await()
            .toObjects(Post::class.java)
    }

    fun updateUnavailableTime(post: Post) {
        val postCollection = db.collection("posts")
        val postDocument = postCollection.document(post.id.toString())

        postDocument.set(post)
    }
}