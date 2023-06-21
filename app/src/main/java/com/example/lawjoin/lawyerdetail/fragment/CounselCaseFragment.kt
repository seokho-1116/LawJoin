package com.example.lawjoin.lawyerdetail.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.R
import com.example.lawjoin.data.model.Post
import com.example.lawjoin.lawyerdetail.adapter.CounselCaseAdapter

@RequiresApi(Build.VERSION_CODES.O)
class CounselCaseFragment(val counselCases: List<Post>): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_counsel_case, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupLawyerInfo(view)
    }

    private fun setupLawyerInfo(view: View) {
        val rv: RecyclerView = view.findViewById(R.id.rv_counsel_case)
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.adapter = CounselCaseAdapter(counselCases)
    }
}