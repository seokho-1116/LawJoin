package com.example.lawjoin.lawyer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.R
import android.content.Context
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import com.example.lawjoin.data.objects.CategoryObjects
import com.example.lawjoin.data.objects.LawyerObjects

class CategoryAdapter(private val categorySet: ArrayList<CategoryObjects>, val context: Context, val lawyerObjects: ArrayList<LawyerObjects>)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(), Filterable {

    private var excelSearchList: List<LawyerObjects>? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemBoardContent: Button
        init {
            itemBoardContent = view.findViewById(R.id.item_board_content)
        }

        fun bind(item: CategoryObjects) {
            itemBoardContent.text = item.category

            itemView.setOnClickListener {
                Toast.makeText(context, "시발", Toast.LENGTH_SHORT).show()
            }
        }
    }

    init{
        this.excelSearchList = lawyerObjects
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.lawyer_category_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(categorySet[position])
    }
    override fun getItemCount() = categorySet.size

    override fun getFilter() : Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()){
                    excelSearchList = lawyerObjects
                } else {
                    val filteredList = ArrayList<LawyerObjects>()

                    for (row in lawyerObjects){
                        if (row.name.toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredList.add(row)
                        }
                    }
                    excelSearchList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = excelSearchList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                excelSearchList = filterResults.values as ArrayList <LawyerObjects>
                notifyDataSetChanged()
            }
        }
    }



}

