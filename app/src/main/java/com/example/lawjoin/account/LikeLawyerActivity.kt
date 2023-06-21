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
import com.example.lawjoin.databinding.ActivityLawyerLikeBinding
import com.example.lawjoin.lawyer.LawyerListViewModel
import com.example.lawjoin.lawyer.adapter.LawyerListAdapter

class LikeLawyerActivity : AppCompatActivity() {
    private lateinit var lawyerListViewModel: LawyerListViewModel
    private lateinit var lawyerListAdapter: LawyerListAdapter
    private lateinit var binding: ActivityLawyerLikeBinding
    private lateinit var currentUser: AuthUserDto

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerLikeBinding.inflate(layoutInflater)

        AuthUtils.getCurrentUser { authUserDto, _ ->
            currentUser = authUserDto!!
        }

        lawyerListViewModel = ViewModelProvider(this, ViewModelFactory())[LawyerListViewModel::class.java]
        lawyerListViewModel.getLikeLawyer(currentUser.uid!!)
        lawyerListViewModel.like_lawyers.observe(this) { lawyers ->
            lawyerListAdapter = LawyerListAdapter(lawyers, this)

            binding.rvLawyerList.adapter = lawyerListAdapter
        }

        val lawyerLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvLawyerList.layoutManager = lawyerLayoutManager

        setContentView(binding.root)
    }
}