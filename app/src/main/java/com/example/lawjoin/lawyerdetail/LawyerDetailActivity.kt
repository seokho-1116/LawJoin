package com.example.lawjoin.lawyerdetail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.lawjoin.R
import com.example.lawjoin.chat.ChatRoomActivity
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.data.model.ChatRoom
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.data.model.Person
import com.example.lawjoin.data.repository.ChatRoomRepository
import com.example.lawjoin.databinding.ActivityLawyerDetailBinding
import com.example.lawjoin.lawyerdetail.adapter.ViewPagerAdapter
import com.example.lawjoin.lawyerdetail.fragment.CounselCaseFragment
import com.example.lawjoin.lawyerdetail.fragment.CounselReviewFragment
import com.example.lawjoin.lawyerdetail.fragment.LawyerInfoFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
open class LawyerDetailActivity : AppCompatActivity() {
    private val chatRoomRepository = ChatRoomRepository.getInstance()
    private lateinit var binding : ActivityLawyerDetailBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var lawyerDetailViewModel : LawyerDetailViewModel
    private val tabName: List<String> = listOf("변호사 정보", "상담 사례", "상담 후기")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupProperties()

        //TODO: uid 받아오기
        val position = "uid"
        val adapter = ViewPagerAdapter(this)
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

            setupListener(lawyer)
            setContentView(binding.root)
        }

        setupLikeButton()

        binding.vpLawyerInfo.adapter = adapter
        TabLayoutMediator(binding.lyLawyerInfoTab, binding.vpLawyerInfo) { tab, position ->
            tab.text = tabName["$position".toInt()]
        }.attach()

        setContentView(binding.root)
    }

    private fun setupProperties() {
        binding = ActivityLawyerDetailBinding.inflate(layoutInflater)

        //TODO: get Lawyer Data previous page

        auth = Firebase.auth
        currentUser = auth.currentUser!!

        lawyerDetailViewModel =
            ViewModelProvider(this, ViewModelFactory())[LawyerDetailViewModel::class.java]
    }

    private fun setupLikeButton() {
        val button = binding.btnLawyerLike
        val initialDrawable: Drawable? = button.compoundDrawablesRelative[0]
        val pressedDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_red_heart)
        var isButtonPressed = false
        button.setOnClickListener {
            isButtonPressed = !isButtonPressed

            val drawable = if (isButtonPressed) pressedDrawable else initialDrawable
            button.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    private fun setupListener(lawyer: Lawyer) {
        binding.btnLawyerShare.setOnClickListener {
            val data = lawyer.toData()
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, data)
            }
            startActivity(Intent.createChooser(intent, data))
        }

        binding.btnChatStart.setOnClickListener {
            addChatRoom(lawyer)
        }
        binding.btnReserveCounsel.setOnClickListener {
            //TODO: start reservation
            intent.putExtra("lawyer", lawyer)
            startActivity(intent)
        }
    }

    private fun addChatRoom(lawyer: Lawyer) {
        val chatRoom = ChatRoom(listOf(currentUser.uid,lawyer.uid!!), mutableListOf())

        chatRoomRepository.findUserChatRoomsByKey(currentUser.uid) { it ->

            val isChatRoomExist = it.children.any {
                it.child("users").children.any { reference ->
                    reference.value == lawyer.uid
                }
            }

            if (isChatRoomExist) {
                goToChatRoom(chatRoom, lawyer)
            } else {
                chatRoomRepository.saveChatRoomUnder(currentUser.uid) { reference ->
                    reference.push().setValue(chatRoom).addOnSuccessListener {
                        goToChatRoom(chatRoom, lawyer)
                    }
                }
            }
        }
    }

    private fun goToChatRoom(chatRoom: ChatRoom, opponentUid: Person) {
        val intent = Intent(applicationContext, ChatRoomActivity::class.java)
        intent.putExtra("ChatRoom", chatRoom)
        intent.putExtra("receiver", opponentUid)
        intent.putExtra("ChatRoomKey", "")
        startActivity(intent)
    }
}
