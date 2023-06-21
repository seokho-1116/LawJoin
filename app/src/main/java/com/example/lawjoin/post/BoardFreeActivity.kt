package com.example.lawjoin.post

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawjoin.MainActivity
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.databinding.ActivityFreeBoardListBinding
import com.example.lawjoin.lawyer.LawyerListActivity
import com.example.lawjoin.word.LawWordListActivity

@RequiresApi(Build.VERSION_CODES.O)
open class BoardFreeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFreeBoardListBinding
    private lateinit var postViewModel: PostViewModel
    lateinit var postadapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreeBoardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rvPostList = binding.rvPostList

        postViewModel = ViewModelProvider(this, ViewModelFactory())[PostViewModel::class.java]
        postViewModel.findAllFreePosts("free_post")

        postViewModel.posts.observe(this) { posts ->
            postadapter = PostAdapter(posts, this)
            rvPostList.adapter = postadapter

            binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    postadapter.filter.filter(query)
                    return false
                }
            })
        }

        val postLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvPostList.setHasFixedSize(true)
        rvPostList.layoutManager = postLayoutManager

        binding.swFreeBoard.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                startActivity(Intent(this, BoardCounselActivity::class.java))
                finish()
            }
        }

        binding.btnWrite.setOnClickListener {
            val intent = Intent(applicationContext, WritePostActivity::class.java)
            intent.putExtra("type", "free_post")
            startActivity(intent)
        }

        initializeListener()
    }

    private fun initializeListener() {
        binding.ibMainMessage.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.ibMainLawyer.setOnClickListener {
            startActivity(Intent(this, LawyerListActivity::class.java))
        }
        binding.ibMainLawWord.setOnClickListener {
            startActivity(Intent(this, LawWordListActivity::class.java))
        }
        binding.ibMainPost.setOnClickListener {
            startActivity(Intent(this, BoardFreeActivity::class.java))
        }

        binding.ibMainPost.isClickable = false
        binding.ibMainPost.isSelected = true
    }
}