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
import androidx.core.content.ContextCompat
import com.example.lawjoin.data.objects.CategoryObjects
import com.example.lawjoin.data.objects.LawyerObjects

class CategoryAdapter (private val categorySet: ArrayList<CategoryObjects>,
                       private val context: Context,
                       private val lawyerListAdapter: LawyerListAdapter)
    : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selectedCategory: String? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemBoardContent: Button
        init {
            itemBoardContent = view.findViewById(R.id.item_board_content)
        }

        fun bind(item: CategoryObjects) {
            itemBoardContent.text = item.category

            if (item.isSelected) {
                itemBoardContent.setBackgroundResource(androidx.appcompat.R.drawable.abc_btn_default_mtrl_shape)
                itemBoardContent.setTextColor(ContextCompat.getColor(context, R.color.white))
            } else {
                itemBoardContent.setBackgroundResource(androidx.appcompat.R.drawable.abc_btn_default_mtrl_shape)
                itemBoardContent.setTextColor(ContextCompat.getColor(context, R.color.black))
            }

            itemBoardContent.setOnClickListener {
                Toast.makeText(context, "버튼 시발", Toast.LENGTH_SHORT).show()
                val clickedCategory = categorySet[adapterPosition].category
                if (selectedCategory == clickedCategory) {

                    selectedCategory = null
                    lawyerListAdapter.clearFilter()
                } else {
                    selectedCategory = clickedCategory
                    lawyerListAdapter.filter.filter(clickedCategory)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.lawyer_category_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(categorySet!![position])
    }
    override fun getItemCount() = categorySet!!.size
}
