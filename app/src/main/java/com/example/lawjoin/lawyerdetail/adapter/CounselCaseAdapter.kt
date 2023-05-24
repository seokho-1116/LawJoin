package com.example.lawjoin.lawyerdetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.R
import com.example.lawjoin.data.model.CounselCase

class CounselCaseAdapter(private val counselCases: List<CounselCase>):
    RecyclerView.Adapter<CounselCaseAdapter.CounselCaseViewHolder>() {

    class CounselCaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView

        init {
            titleTextView = view.findViewById(R.id.tv_counsel_case_title)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CounselCaseViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.counsel_case, viewGroup, false)

        return CounselCaseViewHolder(view)
    }

    override fun getItemCount() = counselCases.size

    override fun onBindViewHolder(holder: CounselCaseViewHolder, position: Int) {
        holder.titleTextView.text = counselCases[position].title
    }
}