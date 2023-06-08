package com.example.lawjoin.lawyer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.R
import android.content.Context
import android.widget.Filter
import android.widget.Filterable
import com.example.lawjoin.data.model.Lawyer

class CategoryAdapter(private val categories: List<String>, val context: Context, val lawyers: List<Lawyer>)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(), Filterable {

    private var excelSearchList: List<Lawyer>? = null

    init {
        this.excelSearchList = lawyers
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemBoardContent: Button
        init {
            itemBoardContent = view.findViewById(R.id.item_board_content)
        }

        fun bind(item: String) {
            itemBoardContent.text = item

            itemView.setOnClickListener {
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.lawyer_category_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(categories[position])
    }

    override fun getItemCount() = categories.size

    override fun getFilter() : Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                excelSearchList = if (charString.isEmpty()){
                    lawyers
                } else {
                    val filteredList = mutableListOf<Lawyer>()

                    for (row in lawyers){
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = excelSearchList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                excelSearchList = filterResults.values as MutableList<Lawyer>
                notifyDataSetChanged()
            }
        }
    }
}

