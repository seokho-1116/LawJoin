package com.example.lawjoin.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.R
import com.example.lawjoin.data.model.Comment
import com.example.lawjoin.databinding.CommentItemBinding

class PostDetailAdapter :
    RecyclerView.Adapter<PostDetailAdapter.CommentViewHolder>() {
    private val comments: MutableList<Comment> = mutableListOf()

    class CommentViewHolder(itemView: CommentItemBinding)
        : RecyclerView.ViewHolder(itemView.root) {
        val title = itemView.tvCommentTitle
        val writer = itemView.tvCommentWriter
        val detail = itemView.tvCommentDetail
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
            as CommentItemBinding
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.title.text = comments[position].title
        holder.writer.text = comments[position].owner
        holder.detail.text = comments[position].detail
    }

    override fun getItemCount(): Int = comments.size
}