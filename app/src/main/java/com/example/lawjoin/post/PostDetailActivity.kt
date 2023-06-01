package com.example.lawjoin.post

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.data.model.Post
import com.example.lawjoin.data.repository.PostRepository
import com.example.lawjoin.databinding.ActivityPostDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostDetailActivity : AppCompatActivity() {
    private var postRepository: PostRepository = PostRepository.getInstance()
    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var postDetailViewModel: PostDetailViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()

        auth = Firebase.auth

        postDetailViewModel = ViewModelProvider(this, ViewModelFactory())[PostDetailViewModel::class.java]
        postDetailViewModel.findUser(auth.currentUser!!.uid)
        postDetailViewModel.user.observe(this) {
            if (it.bookmarkedPost.any { bookmarkedPost -> bookmarkedPost == postId}) {
                binding.btnBookmarkPost.isSelected = true
            }

            if (it.recommendPost.any { recommendedPost -> recommendedPost == postId }) {
                binding.btnRecommendPost.isSelected = true
            }
        }

        binding.btnBookmarkPost.setOnClickListener {
            it.isSelected = !it.isSelected
        }

        binding.btnWriteComment.setOnClickListener {
            it.isSelected = !it.isSelected
        }

        postRepository.savePost {
            it.child("post").push().setValue(Post())
        }

        val posts: MutableList<Post> = mutableListOf()
        postRepository.findAllPost {
            posts.add(it.getValue(Post::class.java)!!)
        }
    }

    private fun initViews() {
        binding = ActivityPostDetailBinding.inflate(layoutInflater)

        binding.rvPostList.layoutManager = LinearLayoutManager(this)
        binding.rvPostList.adapter
    }

    override fun onBackPressed() {
        if (binding.btnRecommendPost.isPressed) {
            postDetailViewModel
        }
    }
}