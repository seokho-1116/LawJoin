package com.example.lawjoin.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.lawjoin.data.model.Comment
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
        private const val TAG = "PostRepository"

        fun getInstance(): PostRepository {
            return INSTANCE
        }
    }

    fun savePost(category: String, callback: (DatabaseReference) -> Task<Void>) {
        callback(databaseReference.child(category).push())
    }

    fun findPost(category: String, postId: String, callback: (DataSnapshot) -> Unit) {
        databaseReference
            .child(category)
            .child(postId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Query canceled or encountered an error: ${error.message}")
                }
            })
    }

    fun findAllPosts(category: String, callback: (List<Post>) -> Unit) {
        databaseReference
            .child(category)
            .addValueEventListener(object : ValueEventListener {
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
                    Log.e(TAG, "Query canceled or encountered an error: ${error.message}")
                }
            })
    }

    fun saveCounselPost(callback: (DatabaseReference) -> Task<Void>) {
        callback(databaseReference.child("counselPost").push())
    }

    fun updatePostComment(category: String, postId: String, comment: Comment) {
        databaseReference.child(category)
            .child(postId)
            .child("comments")
            .push()
            .setValue(comment)
    }

    fun updatePostRecommendCount(category: String, post: Post) {
        val reference = databaseReference.child(category)
            .child(post.id)
            .child("recommendationCount")

        reference
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val previousValue = dataSnapshot.getValue(Int::class.java)
                    val updatedValue = previousValue?.plus(1) ?: 1
                    reference.setValue(updatedValue)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Query canceled or encountered an error: ${error.message}")
                }
            })
    }

    fun findCommentsUnderPost(category: String, postId: String, callback: (DataSnapshot) -> Unit) {
        databaseReference
            .child(category)
            .child(postId)
            .child("comments")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    callback(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Query canceled or encountered an error: ${error.message}")
                }
            })
    }

    fun findBookmarkedPost(uid: String, callback: (List<Post>) -> Unit) {
        Firebase.database.reference.child("users")
            .child("user")
            .child(uid)
            .child("bookmarked_posts")
            .addValueEventListener(object : ValueEventListener {
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
                    TODO("Not yet implemented")
                }
            })

    }

    fun findMyPost(uid: String, callback: (List<Post>) -> Unit) {
        val postsRef = databaseReference

        postsRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts = mutableListOf<Post>()
                for (category in snapshot.children) {
                    for (post in category.children) {
                        val ownerId = post.child("ownerId").getValue(String::class.java)!!
                        if(ownerId == uid) {
                            posts.add(post.getValue(Post::class.java)!!)
                        }
                    }
                }
                callback(posts)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}