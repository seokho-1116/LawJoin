package com.example.lawjoin.account

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawjoin.common.AuthUtils
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.data.model.AuthUserDto
import com.example.lawjoin.databinding.ActivityBookmarkedPostActivityBinding
import com.example.lawjoin.post.PostAdapter
import com.example.lawjoin.post.PostViewModel

class BookmarkedPostActivity : AppCompatActivity() {
    private lateinit var postViewModel: PostViewModel
    private lateinit var postadapter: PostAdapter
    private lateinit var binding: ActivityBookmarkedPostActivityBinding
    private lateinit var currentUser: AuthUserDto

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookmarkedPostActivityBinding.inflate(layoutInflater)

        AuthUtils.getCurrentUser { authUserDto, _ ->
            currentUser = authUserDto!!
        }

        postViewModel = ViewModelProvider(this, ViewModelFactory())[PostViewModel::class.java]
        postViewModel.findBookmarkedPost(currentUser.uid!!)
        postViewModel.bookmarkedPost.observe(this) { counselPosts ->
            postadapter = PostAdapter(counselPosts, this)
            binding.rvPostList.adapter = postadapter
        }

        val postLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvPostList.setHasFixedSize(true)
        binding.rvPostList.layoutManager = postLayoutManager

        setContentView(binding.root)
    }
}