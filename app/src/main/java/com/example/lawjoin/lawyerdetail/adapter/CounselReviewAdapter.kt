package com.example.lawjoin.lawyerdetail.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.data.model.CounselReview
import com.example.lawjoin.databinding.CounselReviewItemBinding
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class CounselReviewAdapter(private val counselReviews: List<CounselReview>):
    RecyclerView.Adapter<CounselReviewAdapter.CounselReviewViewHolder>() {
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    class CounselReviewViewHolder(itemView: CounselReviewItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val titleTextView: TextView = itemView.tvCounselReviewTitle
        val detailTextView: TextView = itemView.tvCounselReviewDetail
        val writerNameTextView: TextView = itemView.tvCounselReviewId
        val createdTimeTextView: TextView = itemView.tvCounselDate
        val category: TextView = itemView.tvCounselCategory
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CounselReviewViewHolder {
        val view = CounselReviewItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return CounselReviewViewHolder(view)
    }

    override fun getItemCount() = counselReviews.size

    override fun onBindViewHolder(holder: CounselReviewViewHolder, position: Int) {

        holder.category.text = counselReviews[position].category
        holder.titleTextView.text = counselReviews[position].title
        holder.detailTextView.text = counselReviews[position].detail
        holder.writerNameTextView.text = counselReviews[position].writerId
        holder.createdTimeTextView.text =
            ZonedDateTime
                .parse(counselReviews[position].createdTime, formatter)
                .withZoneSameInstant(ZoneId.systemDefault())
                .toLocalDate()
                .toString()
    }
}