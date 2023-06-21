package com.example.lawjoin.lawyer

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawjoin.MainActivity
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.data.repository.LawyerRepository
import com.example.lawjoin.databinding.ActivityLawyerListBinding
import com.example.lawjoin.lawyer.adapter.LawyerListAdapter
import com.example.lawjoin.post.BoardFreeActivity
import com.example.lawjoin.word.LawWordListActivity

@RequiresApi(Build.VERSION_CODES.O)
class LawyerListActivity : AppCompatActivity() {
    private lateinit var lawyerRepository: LawyerRepository
    private lateinit var lawyerListAdapter: LawyerListAdapter
    private lateinit var searchView: SearchView
    private lateinit var binding: ActivityLawyerListBinding
    private lateinit var lawyerListViewModel: LawyerListViewModel
    private lateinit var searchViewTextListener: SearchView.OnQueryTextListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lawyerRepository = LawyerRepository.getInstance()

        val rvLawyerList = binding.rvLawyerList
        searchView = binding.edtChatSearch

        lawyerListViewModel = ViewModelProvider(this, ViewModelFactory())[LawyerListViewModel::class.java]
        lawyerListViewModel.getAllLawyers()
        lawyerListViewModel.lawyers.observe(this) { lawyers ->
            lawyerListAdapter = LawyerListAdapter(lawyers, this)

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

        rvLawyerList.layoutManager = lawyerLayoutManager

        initializeListener()
    }

    private fun initializeListener() {
        binding.btnFreeCounsel.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:031-310-2929"))
            startActivity(intent)
        }

        binding.ibMainLawyer.setOnClickListener {
            startActivity(Intent(this, LawyerListActivity::class.java))
        }
        binding.ibMainMessage.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        binding.ibMainLawWord.setOnClickListener {
            startActivity(Intent(this, LawWordListActivity::class.java))
        }
        binding.ibMainPost.setOnClickListener {
            startActivity(Intent(this, BoardFreeActivity::class.java))
        }
        binding.ibMainLawyer.isClickable = false
        binding.ibMainLawyer.isSelected = true
    }
}