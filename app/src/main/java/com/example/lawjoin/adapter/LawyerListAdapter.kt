package com.example.lawjoin.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lawjoin.LawyerProfileDetailActivity
import com.example.lawjoin.R
import com.example.lawjoin.data.objects.LawyerObjects
import java.io.Serializable
import kotlin.collections.ArrayList

class LawyerListAdapter(private val lawyerObjects: ArrayList<LawyerObjects>, var context: Context)
    : RecyclerView.Adapter<LawyerListAdapter.ViewHolder>(), Filterable {

    private var excelSearchList: List<LawyerObjects>? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lawyerName: TextView
        val lawyerSummary: TextView
        val lawyerReview: TextView
        val lawyerCategory: TextView
        val lawyerImage: ImageView
        init {
            lawyerName = view.findViewById(R.id.tv_lawyer_name)
            lawyerSummary = view.findViewById(R.id.tv_lawyer_summary)
            lawyerReview = view.findViewById(R.id.tv_lawyer_review)
            lawyerCategory = view.findViewById(R.id.tv_lawyer_category)
            lawyerImage = view.findViewById(R.id.iv_lawyer_image)
        }

        fun bind(item: LawyerObjects) {
            lawyerName.text = item.name
            lawyerSummary.text = item.summary
            lawyerReview.text = item.review
            lawyerCategory.text = item.category
            Glide.with(itemView).load(item.imageResId).into(lawyerImage)

            itemView.setOnClickListener {
                Toast.makeText(context, "시발", Toast.LENGTH_SHORT).show()
                Intent(context, LawyerProfileDetailActivity::class.java).apply {
                    putExtra("data", item as Serializable)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }

    init{
        this.excelSearchList = lawyerObjects
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LawyerListAdapter.ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.lawyer_thumbnail, viewGroup, false)

        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(excelSearchList!![position])
    }
    override fun getItemCount() = excelSearchList!!.size

    override fun getFilter() : Filter{
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

