package com.example.lawjoin.word

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.MainActivity
import com.example.lawjoin.data.repository.LawWordRepository
import com.example.lawjoin.databinding.ActivityLawWordListBinding
import com.example.lawjoin.lawyer.LawyerListActivity
import com.example.lawjoin.post.PostDetailActivity


//TODO: 법률 용어 리스트
@RequiresApi(Build.VERSION_CODES.O)
class LawWordListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LawWordListAdapter
    private lateinit var binding: ActivityLawWordListBinding
    private lateinit var dataSource: LawWordRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawWordListBinding.inflate(layoutInflater)

        recyclerView = binding.rvLawWordList
        adapter = LawWordListAdapter()

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        dataSource = LawWordRepository()

        loadInitialLawWords()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (!adapter.isSearchViewActive() && lastVisibleItemPosition == totalItemCount - 1) {
                    loadMoreLawWords()
                }
            }
        })

        val searchViewTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(s: String): Boolean {
                    adapter.filter.filter(s)
                    return false
                }
            }

        binding.edtChatSearch.setOnQueryTextListener(searchViewTextListener)

        initializeListener()
        setContentView(binding.root)
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
            startActivity(Intent(this, PostDetailActivity::class.java))
        }

        binding.ibMainLawWord.isClickable = false
        binding.ibMainLawWord.isSelected = true
    }

    private fun loadInitialLawWords() {
        dataSource.loadInitialLawWords { lawWords ->
            adapter.setLawWords(lawWords)
        }
    }

    private fun loadMoreLawWords() {
        dataSource.loadMoreLawWords { lawWords ->
            adapter.addLawWords(lawWords)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}