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
    private lateinit var searchView: SearchView
    private lateinit var lawyerListViewModel: LawyerListViewModel
    private lateinit var searchViewTextListener: SearchView.OnQueryTextListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLawyerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lawyerRepository = LawyerRepository.getInstance()

        val rvCategoryList = binding.rvCategoryList
        val rvLawyerList = binding.rvLawyerList
        searchView = binding.edtChatSearch

        lawyerListViewModel = ViewModelProvider(this, ViewModelFactory())[LawyerListViewModel::class.java]
        lawyerListViewModel.getAllLawyers()
        lawyerListViewModel.lawyers.observe(this) { lawyers ->
            categoryAdapter = CategoryAdapter(
                lawyers.flatMap { lawyer -> lawyer.categories }.distinct(),
                this,
                lawyers
            )
            lawyerListAdapter = LawyerListAdapter(lawyers, this)

            rvCategoryList.adapter = categoryAdapter
            rvLawyerList.adapter = lawyerListAdapter

            searchViewTextListener =
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(s: String): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(s: String): Boolean {
                        lawyerListAdapter.filter.filter(s)
                        return false
                    }
                }
            searchView.setOnQueryTextListener(searchViewTextListener)
        }

        val lawyerLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val categoryLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvCategoryList.layoutManager = categoryLayoutManager
        rvLawyerList.layoutManager = lawyerLayoutManager
    }
}