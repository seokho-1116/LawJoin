package com.example.lawjoin.lawyerdetail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.R
import com.example.lawjoin.data.model.CounselReview

class CounselReviewAdapter(private val counselReviews: List<CounselReview>):
    RecyclerView.Adapter<CounselReviewAdapter.CounselReviewViewHolder>() {

    class CounselReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView
        val detailTextView: TextView
        val writerNameTextView: TextView
        val createdTimeTextView: TextView

        init {
            titleTextView = view.findViewById(R.id.tv_counsel_case_title)
            detailTextView = view.findViewById(R.id.tv_counsel_review_detail)
            writerNameTextView = view.findViewById(R.id.tv_counsel_review_id)
            createdTimeTextView = view.findViewById(R.id.tv_counsel_date)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CounselReviewViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.counsel_case_item, viewGroup, false)

        return CounselReviewViewHolder(view)
    }

    override fun getItemCount() = counselReviews.size

    override fun onBindViewHolder(holder: CounselReviewViewHolder, position: Int) {
        holder.titleTextView.text = counselReviews[position].title
        holder.detailTextView.text = counselReviews[position].detail
        holder.writerNameTextView.text = counselReviews[position].writerName
        holder.createdTimeTextView.text = counselReviews[position].createdTime.toString()
    }
}