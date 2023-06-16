package com.example.lawjoin.lawyerdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.data.model.Post
import com.example.lawjoin.databinding.CounselCaseItemBinding

class CounselCaseAdapter(private val counselCases: List<Post>):
    RecyclerView.Adapter<CounselCaseAdapter.CounselCaseViewHolder>() {

    inner class CounselCaseViewHolder(val binding: CounselCaseItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.tvCounselCaseTitle.text = counselCases[position].title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounselCaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CounselCaseItemBinding.inflate(inflater, parent, false)
        return CounselCaseViewHolder(binding)
    }

    override fun getItemCount() = counselCases.size

    override fun onBindViewHolder(holder: CounselCaseViewHolder, position: Int) {
        holder.bind(position)
    }
}