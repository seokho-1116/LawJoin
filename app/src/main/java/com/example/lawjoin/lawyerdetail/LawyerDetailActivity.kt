package com.example.lawjoin.lawyerdetail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.lawjoin.R
import com.example.lawjoin.chat.ChatRoomActivity
import com.example.lawjoin.chat.RecyclerChatAdapter
import com.example.lawjoin.common.AuthUtils
import com.example.lawjoin.common.FireBaseStorageUtils
import com.example.lawjoin.common.ViewModelFactory
import com.example.lawjoin.counselreservation.CounselReservationActivity
import com.example.lawjoin.data.model.AuthUserDto
import com.example.lawjoin.data.model.ChatRoom
import com.example.lawjoin.data.model.Lawyer
import com.example.lawjoin.data.model.LawyerDto
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
import com.google.firebase.storage.ktx.storage
import java.io.Serializable

@RequiresApi(Build.VERSION_CODES.O)
open class LawyerDetailActivity : AppCompatActivity() {
    private val chatRoomRepository = ChatRoomRepository.getInstance()
    private lateinit var binding : ActivityLawyerDetailBinding
    private lateinit var currentUser : AuthUserDto
    private lateinit var lawyerDetailViewModel : LawyerDetailViewModel
    private lateinit var lawyer: Lawyer
    private val tabName: List<String> = listOf("변호사 정보", "상담 사례", "상담 후기")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupProperties()

        if (lawyer.uid == "GPT" || lawyer.uid == "BOT") {
          binding.btnChatStart.visibility = View.INVISIBLE
          binding.btnReserveCounsel.visibility = View.INVISIBLE
        }

        val adapter = ViewPagerAdapter(this)
        adapter.addFragment(LawyerInfoFragment(lawyer))
        adapter.addFragment(CounselCaseFragment(lawyer.counselCases))
        adapter.addFragment(CounselReviewFragment(lawyer.counselReviews))

        binding.tvLawyerDetailName.text = lawyer.name
        for (category in lawyer.categories) {
            val tv = TextView(this)
            tv.text = category
            tv.setTextSize(Dimension.SP, 14.0F)
            tv.setBackgroundResource(R.drawable.bg_lawyer_list_category)
            binding.lyLawyerCategory.addView(tv)
        }

        setupListener(lawyer)
        setContentView(binding.root)
        setupLikeButton()

        binding.vpLawyerInfo.adapter = adapter
        TabLayoutMediator(binding.lyLawyerInfoTab, binding.vpLawyerInfo) { tab, position ->
            tab.text = tabName["$position".toInt()]
        }.attach()

        setContentView(binding.root)
    }

    private fun setupProperties() {
        binding = ActivityLawyerDetailBinding.inflate(layoutInflater)

        AuthUtils.getCurrentUser { authUserDto, _ ->
            currentUser = authUserDto!!
        }

        lawyerDetailViewModel =
            ViewModelProvider(this, ViewModelFactory())[LawyerDetailViewModel::class.java]
        val intent = intent
        lawyer = intent.serializable("lawyer")!!


        FireBaseStorageUtils.setupProfile(lawyer.uid, lawyer.profile_url) {
            setProfileAndConfigureScreen(it)
        }
    }

    private fun setProfileAndConfigureScreen(url: String) {
        Glide.with(this)
            .load(url)
            .error(R.drawable.ic_lawyer_basic)
            .override(150, 200)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivLawyerProfileImage)
    }

    private fun setupLikeButton() {
        binding.btnLawyerLike.setOnClickListener {
            it.isSelected = !it.isSelected
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
            val intent = Intent(this, CounselReservationActivity::class.java)
            intent.putExtra("lawyer", lawyer)
            startActivity(intent)
        }
    }

    private fun addChatRoom(lawyer: Lawyer) {
        val chatRoom = ChatRoom(mapOf(), listOf(currentUser.uid!!, lawyer.uid))

        chatRoomRepository.findUserChatRoomsByKey(currentUser.uid!!) { it ->
            val isChatRoomExist = it.children.any {
                it.child("users").children.any { reference ->
                    reference.value == lawyer.uid
                }
            }

            if (isChatRoomExist) {
                goToChatRoom(chatRoom, LawyerDto(lawyer.uid, lawyer.name, lawyer.email))
            } else {
                chatRoomRepository.saveChatRoomUnder(currentUser.uid!!) { reference ->
                    reference.push().setValue(chatRoom).addOnSuccessListener {
                        goToChatRoom(chatRoom, LawyerDto(lawyer.uid, lawyer.name, lawyer.email))
                    }
                }
            }
        }
    }

    private fun goToChatRoom(chatRoom: ChatRoom, lawyer: LawyerDto) {
        val intent = Intent(applicationContext, ChatRoomActivity::class.java)
        intent.putExtra("ChatRoom", chatRoom)
        intent.putExtra("receiver", lawyer)
        intent.putExtra("ChatRoomKey",currentUser.uid)
        startActivity(intent)
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }
}
