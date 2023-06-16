package com.example.lawjoin.lawyerdetail.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.R
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.data.model.CounselReview
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.lawyerdetail.LawyerDetailViewModel
import com.example.lawjoin.lawyerdetail.adapter.CounselReviewAdapter

@RequiresApi(Build.VERSION_CODES.O)
class CounselReviewFragment(private val lawyerId: String,
                            private val counselReviews: List<CounselReview>): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_counsel_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupLawyerInfo(view)
    }

    private fun setupLawyerInfo(view: View) {
        val rv: RecyclerView = view.findViewById(R.id.rv_counsel_review)
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.adapter = CounselReviewAdapter(counselReviews)

        val button = view.findViewById<Button>(R.id.btn_comment_write)
        button.setOnClickListener {
            val intent = Intent(context, CommentWriteActivity::class.java)
            intent.putExtra("lawyerId", lawyerId)
            startActivity(intent)
        }
    }
}