package com.example.lawjoin.lawyerdetail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.R
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.lawyerdetail.LawyerDetailViewModel
import com.example.lawjoin.lawyerdetail.adapter.CounselCaseAdapter

class CounselCaseFragment(): Fragment() {
    private lateinit var lawyerDetailViewModel : LawyerDetailViewModel
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
        lawyerDetailViewModel = ViewModelProvider(requireActivity(), ViewModelFactory())[LawyerDetailViewModel::class.java]
        lawyerDetailViewModel.lawyer.observe(viewLifecycleOwner) {lawyer ->
            val rv: RecyclerView = view.findViewById(R.id.rv_counsel_case)
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rv.adapter = CounselCaseAdapter(lawyer.counselCases)
        }
    }
}