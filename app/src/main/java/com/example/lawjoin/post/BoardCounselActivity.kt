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
import com.example.lawjoin.databinding.ActivityCounselBoardListBinding
import com.example.lawjoin.lawyer.LawyerListActivity
import com.example.lawjoin.word.LawWordListActivity

@RequiresApi(Build.VERSION_CODES.O)
open class BoardCounselActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCounselBoardListBinding
    private lateinit var postViewModel: PostViewModel
    lateinit var postadapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounselBoardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rvPostList = binding.rvCounselPostList

        postViewModel = ViewModelProvider(this, ViewModelFactory())[PostViewModel::class.java]
        postViewModel.findAllCounselPosts("counsel_post")

        postViewModel.counselPosts.observe(this) { counselPosts ->
            postadapter = PostAdapter(counselPosts, this)
            rvPostList.adapter = postadapter
        }

        val postLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvPostList.setHasFixedSize(true)
        rvPostList.layoutManager = postLayoutManager


        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    postadapter.filter.filter(query)
                    return false
                }
            })

        binding.swCounselBoard.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startActivity(Intent(this, BoardFreeActivity::class.java))
                finish()
            }
        }

        binding.btnWrite.setOnClickListener {
            val intent = Intent(applicationContext, WritePostActivity::class.java)
            intent.putExtra("type", "counsel_post")
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