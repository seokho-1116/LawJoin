package com.example.lawjoin.lawyer.adapter


import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.lawjoin.R
import com.example.lawjoin.common.FireBaseStorageUtils
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.lawyerdetail.LawyerDetailActivity

@RequiresApi(Build.VERSION_CODES.O)
class LawyerListAdapter(private val lawyers: List<Lawyer>, var context: Context)
    : RecyclerView.Adapter<LawyerListAdapter.LawyerListViewHolder>(), Filterable {
    private var excelSearchList: List<Lawyer>? = null

    init {
        this.excelSearchList = lawyers
    }

    inner class LawyerListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val lawyerName: TextView
        private val lawyerSummary: TextView
        private val lawyerReview: TextView
        private val lawyerCategory: TextView
        private val lawyerImage: ImageView
        init {
            lawyerName = view.findViewById(R.id.tv_lawyer_name)
            lawyerSummary = view.findViewById(R.id.tv_lawyer_summary)
            lawyerReview = view.findViewById(R.id.tv_lawyer_review)
            lawyerCategory = view.findViewById(R.id.tv_lawyer_category)
            lawyerImage = view.findViewById(R.id.iv_lawyer_image)
        }

        fun bind(item: Lawyer) {
            lawyerName.text = item.name
            lawyerSummary.text = item.introduce
            lawyerReview.text = item.reviewCount.toString()
            lawyerCategory.text = item.categories.firstOrNull()

            FireBaseStorageUtils.setupProfile(item.uid, item.profileUrl) {
                setProfileAndConfigureScreen(it)
            }

            itemView.setOnClickListener {
                val intent = Intent(context, LawyerDetailActivity::class.java)
                intent.putExtra("lawyerId", item.uid)
                context.startActivity(intent)
            }
        }

        private fun setProfileAndConfigureScreen(url: String) {
            Glide.with(context)
                .load(url)
                .error(R.drawable.ic_lawyer_basic)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(lawyerImage)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LawyerListViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.lawyer_thumbnail_item, viewGroup, false)

        return LawyerListViewHolder(view)
    }

    override fun onBindViewHolder(lawyerListViewHolder: LawyerListViewHolder, position: Int) {
        lawyerListViewHolder.bind(excelSearchList!![position])
    }

    override fun getItemCount() = excelSearchList!!.size

    override fun getFilter() : Filter{
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
                excelSearchList = filterResults.values as List<Lawyer>
                notifyDataSetChanged()
            }
        }
    }
}

