package com.example.lawjoin.data.repository

import com.example.lawjoin.data.model.Post
import com.google.firebase.firestore.FirebaseFirestore

class PostRepository {
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun savePost(post: Post) {
        db.collection("posts")
            .add(post)
    }

    fun findAllPost() {
        var posts: List<Post>
        db.collection("posts")
            .get()
            .addOnSuccessListener {
                document -> document.toObjects(Post::class.java)
            }
    }

    fun findPost(postId: Int) {
        db.collection("posts")
            .document(postId.toString())
            .get().addOnSuccessListener {
            }
    }
}