package com.example.lawjoin.lawyerdetail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.lawjoin.R
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.databinding.ActivityLawyerDetailBinding
import com.example.lawjoin.lawyerdetail.adapter.ViewPagerAdapter
import com.example.lawjoin.lawyerdetail.fragment.CounselCaseFragment
import com.example.lawjoin.lawyerdetail.fragment.CounselReviewFragment
import com.example.lawjoin.lawyerdetail.fragment.LawyerInfoFragment
import com.google.android.material.tabs.TabLayoutMediator

open class LawyerDetailActivity : AppCompatActivity() {
    lateinit var binding : ActivityLawyerDetailBinding
    private lateinit var lawyerDetailViewModel : LawyerDetailViewModel
    private val tabName: List<String> = listOf("변호사 정보", "상담 사례", "상담 후기")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerDetailBinding.inflate(layoutInflater)

        //get Lawyer Data previous page

        lawyerDetailViewModel = ViewModelProvider(this, ViewModelFactory())[LawyerDetailViewModel::class.java]

        val adapter = ViewPagerAdapter(this)

        val position = 1
        lawyerDetailViewModel.getLawyer(position)
        lawyerDetailViewModel.lawyer.observe(this) { lawyer ->
            adapter.addFragment(LawyerInfoFragment())
            adapter.addFragment(CounselCaseFragment())
            adapter.addFragment(CounselReviewFragment())
            binding.tvLawyerDetailName.text = lawyer.name
            for (category in lawyer.categories) {
                val tv = TextView(this)
                tv.text = category
                tv.setTextSize(Dimension.SP, 8.0F)
                tv.setBackgroundResource(R.drawable.bg_lawyer_list_category)
                binding.lyLawyerCategory.addView(tv)
            }

            binding.btnLawyerShare.setOnClickListener {
                val data = lawyer.toData()
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, data)
                }
                startActivity(Intent.createChooser(intent, data))
            }
            binding.btnChatStart.setOnClickListener {
                //TODO: start chat
                intent.putExtra("lawyer", lawyer)
                startActivity(intent)
            }
            binding.btnReserveCounsel.setOnClickListener {
                //TODO: start reservation
                intent.putExtra("lawyer", lawyer)
                startActivity(intent)
            }
        }

        val button = binding.btnLawyerLike

        val initialDrawable: Drawable? = button.compoundDrawablesRelative[0]

        val pressedDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_red_heart)

        var isButtonPressed = false
        button.setOnClickListener {
            isButtonPressed = !isButtonPressed

            // Toggle the drawableStart based on the button's pressed state
            val drawable = if (isButtonPressed) pressedDrawable else initialDrawable
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
        }

        binding.vpLawyerInfo.adapter = adapter

        TabLayoutMediator(binding.lyLawyerInfoTab, binding.vpLawyerInfo) { tab, position ->
            tab.text = tabName["${position}".toInt()]
        }.attach()

        setContentView(binding.root)
    }
}
