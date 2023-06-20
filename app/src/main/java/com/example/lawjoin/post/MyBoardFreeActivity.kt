package com.example.lawjoin.post

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.data.repository.LawyerRepository
import com.example.lawjoin.databinding.ActivityLawyerListBinding
import com.example.lawjoin.lawyer.LawyerListViewModel
import com.example.lawjoin.lawyer.adapter.CategoryAdapter
import com.example.lawjoin.lawyer.adapter.LawyerListAdapter

@RequiresApi(Build.VERSION_CODES.O)
class MyBoardFreeActivity : AppCompatActivity() {
    private lateinit var lawyerRepository: LawyerRepository
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var lawyerListAdapter: LawyerListAdapter
    private lateinit var postAdapter: PostAdapter
    private lateinit var searchView: SearchView
    private lateinit var lawyerListViewModel: LawyerListViewModel
    private lateinit var searchViewTextListener: SearchView.OnQueryTextListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLawyerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lawyerRepository = LawyerRepository.getInstance()

        val myBoardList = binding.rvCategoryList
        searchView = binding.edtChatSearch

        // 이건뭐지
        lawyerListViewModel = ViewModelProvider(this, ViewModelFactory())[LawyerListViewModel::class.java]

        lawyerListViewModel.getAllLawyers()
        lawyerListViewModel.lawyers.observe(this) { lawyers ->
            categoryAdapter = CategoryAdapter(
                lawyers.flatMap { lawyer -> lawyer.categories }.distinct(),
                this,
                lawyers
            )
            lawyerListAdapter = LawyerListAdapter(lawyers, this)

            myBoardList.adapter = postAdapter

            searchViewTextListener =
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(s: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(s: String): Boolean {
                        // postAdapter 에 필터가 없나
                        lawyerListAdapter.filter.filter(s)
                        return false
                    }
                }
            searchView.setOnQueryTextListener(searchViewTextListener)
        }

        val categoryLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        myBoardList.layoutManager = categoryLayoutManager
    }
}