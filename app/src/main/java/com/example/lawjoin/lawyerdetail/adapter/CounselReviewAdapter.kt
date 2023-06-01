package com.example.lawjoin.lawyerdetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.R
import com.example.lawjoin.data.model.CounselReview
import com.example.lawjoin.databinding.CounselReviewItemBinding

class CounselReviewAdapter(private val counselReviews: List<CounselReview>):
    RecyclerView.Adapter<CounselReviewAdapter.CounselReviewViewHolder>() {

    class CounselReviewViewHolder(itemView: CounselReviewItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val titleTextView: TextView = itemView.tvCounselReviewTitle
        val detailTextView: TextView = itemView.tvCounselReviewDetail
        val writerNameTextView: TextView = itemView.tvCounselReviewId
        val createdTimeTextView: TextView = itemView.tvCounselDate
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CounselReviewViewHolder {
        val view = CounselReviewItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return CounselReviewViewHolder(view)
    }

    override fun getItemCount() = counselReviews.size

    override fun onBindViewHolder(holder: CounselReviewViewHolder, position: Int) {
        holder.titleTextView.text = counselReviews[position].title
        holder.detailTextView.text = counselReviews[position].detail
        holder.writerNameTextView.text = counselReviews[position].writerId
        holder.createdTimeTextView.text = counselReviews[position].createdTime
    }
}