package com.example.lawjoin.word

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.data.repository.LawWordRepository
import com.example.lawjoin.databinding.LawWordItemBinding

class LawWordListAdapter : RecyclerView.Adapter<LawWordListAdapter.LawWordViewHolder>(), Filterable {

    private var lawWords = mutableListOf<String>()
    private var filteredLawWords = mutableListOf<String>()
    private var lawWordRepository = LawWordRepository()
    private var isSearchViewActive = false

    fun setLawWords(newLawWords: List<String>) {
        lawWords.clear()
        lawWords.addAll(newLawWords)
        filteredLawWords.clear()
        filteredLawWords.addAll(newLawWords)
        notifyDataSetChanged()
    }

    fun addLawWords(newLawWords: List<String>) {
        val startPos = lawWords.size
        lawWords.addAll(newLawWords)
        filteredLawWords.addAll(newLawWords)
        notifyItemRangeInserted(startPos, newLawWords.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawWordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LawWordItemBinding.inflate(inflater, parent, false)
        return LawWordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LawWordViewHolder, position: Int) {
        val word = filteredLawWords[position]
        holder.bind(word)
    }

    override fun getItemCount(): Int {
        return filteredLawWords.size
    }

    inner class LawWordViewHolder(private val binding: LawWordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(word: String) {
            binding.tvLawWordTitle.text = word
            binding.lyLawWordItem.setOnClickListener {
                val intent = Intent(binding.root.context, LawWordActivity::class.java)
                intent.putExtra("word", word)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.trim() ?: ""
                val filteredList = if (query.isEmpty()) {
                    lawWords.toMutableList()
                } else {
                    let {
                        lawWordRepository.searchLawWords(query)
                    }
                }
                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredLawWords = results?.values as? MutableList<String> ?: mutableListOf()
                isSearchViewActive = constraint?.isNotEmpty() ?: false
                notifyDataSetChanged()
            }
        }
    }

    fun isSearchViewActive(): Boolean {
        return isSearchViewActive
    }
}
