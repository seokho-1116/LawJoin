package com.example.lawjoin.post

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.R
import com.example.lawjoin.data.model.Post
import com.example.lawjoin.databinding.PostItemBinding
import kotlin.collections.ArrayList

class PostAdapter(var posts: List<Post>, var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    var filteredPostList: List<Post> = listOf()

    inner class ViewHolder(itemView: PostItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val title = itemView.tvPostTitle
        fun bind(position: Int) {
            title.text = filteredPostList[position].title
            itemView.setOnClickListener {
                filteredPostList[position].id
//                val intent = Intent(context, PostDetailActivity::class.java)
//                intent.putExtra("post_id", filteredPostList[position].title)
//                context.startActivity(intent)
            }
        }
    }

    init {
        this.filteredPostList = posts
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_item, viewGroup, false)
        return ViewHolder(PostItemBinding.bind(view))
    }

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        (viewholder as ViewHolder).bind(position)
    }

    override fun getItemCount(): Int = filteredPostList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredPostList = posts
                } else {
                    val filteredList = ArrayList<Post>()
                    for (row in posts) {
                        if (row.title.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredPostList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredPostList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredPostList = filterResults.values as ArrayList<Post>
                notifyDataSetChanged()
            }
        }
    }
}