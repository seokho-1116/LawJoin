package com.example.lawjoin.post

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawjoin.common.AuthUtils
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.data.model.AuthUserDto
import com.example.lawjoin.data.model.Comment
import com.example.lawjoin.data.model.Post
import com.example.lawjoin.data.repository.PostRepository
import com.example.lawjoin.data.repository.UserRepository
import com.example.lawjoin.databinding.ActivityPostDetailBinding
import java.time.ZoneId
import java.time.ZonedDateTime

class CounselPostActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    private var postRepository: PostRepository = PostRepository.getInstance()
    private val userRepository = UserRepository.getInstance()
    private lateinit var adapter: FreePostAdapter
    private lateinit var binding: ActivityPostDetailBinding
    private lateinit var postDetailViewModel: PostDetailViewModel
    private lateinit var currentUser: AuthUserDto
    private lateinit var postId: String
    private lateinit var post: Post
    private var isAlreadyNotRecommend: Boolean = true
    private var isAlreadyNotBookmarked: Boolean = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()

        AuthUtils.getCurrentUser { authUserDto, _ ->
            currentUser = authUserDto!!
        }

        postId = intent.getStringExtra("postId")!!

        userRepository.findRecommendedPost(currentUser.uid!!) {
            if (it.contains(postId)) {
                binding.btnRecommendPost.isSelected = true
                isAlreadyNotBookmarked = true
            }
        }

        userRepository.findBookmarkedPost(currentUser.uid!!) {
            if (it.contains(postId)) {
                binding.btnRecommendPost.isSelected = true
                isAlreadyNotBookmarked = true
            }
        }

        postRepository.findPost("counsel_post", postId) {
            post = it.getValue(Post::class.java)!!
            for (comment in it.child("comments").children) {
                post.commentList.add(comment.getValue(Comment::class.java)!!)
            }
            binding.tvPostTitle.text = post.title
            binding.tvPostDetail.text = post.detail

            if (post.commentList.isEmpty()) {
                binding.rvPostList.visibility = View.VISIBLE
                binding.divideLine3.visibility = View.VISIBLE
            }

            adapter = FreePostAdapter(post.commentList)
            binding.rvPostList.adapter = adapter
        }

        postRepository.findCommentsUnderPost("counsel_post", postId) {
            if (binding.rvPostList.adapter != null) {
                val comments: MutableList<Comment>  = mutableListOf()
                for (comment in it.children) {
                    comment.getValue(Comment::class.java)!!.let { comments.add(it) }
                }

                adapter.setComments(comments)
                if (comments.size == 1) {
                    adapter = FreePostAdapter(comments)
                    binding.rvPostList.adapter = adapter
                }
            }
        }

        postDetailViewModel = ViewModelProvider(this, ViewModelFactory())[PostDetailViewModel::class.java]
        postDetailViewModel.findUser(currentUser.uid!!) {
            if (it!!.bookmarkedPosts.any { bookmarkedPostId -> bookmarkedPostId == postId}) {
                binding.btnBookmarkPost.isSelected = true
            }

            if (it.recommendPosts.any { recommendedPostId -> recommendedPostId == postId }) {
                binding.btnRecommendPost.isSelected = true
            }
        }

        binding.btnRecommendPost.setOnClickListener {
            it.isSelected = !it.isSelected
        }

        binding.btnBookmarkPost.setOnClickListener {
            it.isSelected = !it.isSelected
        }

        binding.ibCommentSend.setOnClickListener {
            val comment = Comment("", "", binding.edtCommentInput.text.toString(),
                currentUser.name!!, ZonedDateTime.now(ZoneId.of("UTC")).toString(), "")
            postRepository.updatePostComment("counsel_post", postId, comment)
            binding.edtCommentInput.text.clear()
        }
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {
        super.onBackPressed()
        post.isCounsel = true
        if (binding.btnBookmarkPost.isSelected && isAlreadyNotBookmarked) {
            postDetailViewModel.updateUserBookmark(post)
        } else {
            postDetailViewModel.deleteBookmark(post)
        }

        if (binding.btnRecommendPost.isSelected && isAlreadyNotRecommend) {
            postDetailViewModel.updateUserRecommendPost(post)
            postRepository.updatePostRecommendCount("free_post", post)
        } else {
            postDetailViewModel.deleteRecommend(post)
        }
        finish()
    }

    private fun initViews() {
        binding = ActivityPostDetailBinding.inflate(layoutInflater)

        binding.rvPostList.layoutManager = LinearLayoutManager(this)
    }
}