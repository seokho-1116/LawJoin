package com.example.lawjoin.lawyerdetail

import android.os.Bundle
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lawjoin.R
import com.example.lawjoin.databinding.ActivityLawyerDetailBinding
import com.example.lawjoin.lawyerdetail.adapter.ViewPagerAdapter
import com.example.lawjoin.lawyerdetail.fragment.CounselCaseFragment
import com.example.lawjoin.lawyerdetail.fragment.CounselReviewFragment
import com.example.lawjoin.lawyerdetail.fragment.LawyerInfoFragment
import com.google.android.material.tabs.TabLayoutMediator

open class LawyerDetailActivity : AppCompatActivity() {

    lateinit var mActivityLawyerDetail : ActivityLawyerDetailBinding
    private lateinit var lawyerDetailViewModel : LawyerDetailViewModel
    private val tabName: List<String> = listOf("변호사 정보", "상담 사례", "상담 후기")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityLawyerDetail = ActivityLawyerDetailBinding.inflate(layoutInflater)

        //get Lawyer Data previous page

        lawyerDetailViewModel = ViewModelProvider(this, LawyerDetailViewModelFactory())[LawyerDetailViewModel::class.java]

        val adapter = ViewPagerAdapter(this)

        val position = 1
        lawyerDetailViewModel.getLawyer(position)
        lawyerDetailViewModel.lawyer.observe(this) {
            adapter.addFragment(LawyerInfoFragment(it))
            adapter.addFragment(CounselCaseFragment(it))
            adapter.addFragment(CounselReviewFragment(it))
            mActivityLawyerDetail.tvLawyerDetailName.text = it.name
            for (category in it.categories) {
                val tv = TextView(this)
                tv.text = category
                tv.setTextSize(Dimension.SP, 8.0F)
                tv.setBackgroundResource(R.drawable.bg_lawyer_list_category)
                mActivityLawyerDetail.lyLawyerCategory.addView(tv)
            }
        }

        mActivityLawyerDetail.vpLawyerInfo.adapter = adapter

        TabLayoutMediator(mActivityLawyerDetail.lyLawyerInfoTab, mActivityLawyerDetail.vpLawyerInfo) { tab, position ->
            tab.text = tabName["${position}".toInt()]
        }.attach()

        setContentView(mActivityLawyerDetail.root)
    }
}
