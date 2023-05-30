package com.example.lawjoin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.lawjoin.databinding.ActivityLawWordBinding
import com.example.lawjoin.databinding.ActivityLawyerListBinding
import com.example.lawjoin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
        initializeView()
        initializeListener()
        setupRecycler()
        setContentView(binding.root)
    }

    private fun initializeView() {
        val user = auth.currentUser
        Glide.with(this)
            .load(user?.photoUrl)
            .placeholder(R.drawable.ic_chat_user_default_profile) // Optional placeholder image while loading
            .error(R.drawable.ic_chat_user_default_profile) // Optional error image if the loading fails
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivChatProfile)

        binding.ibMainMessage.isClickable = false
        binding.ibMainMessage.isPressed = true
    }

    private fun initializeListener() {
        //TODO: 변호사 리스트 액티비티 추가
        //TODO: 디자인 일관성으로 하단에 바 추가하기
        binding.btnStartChat.setOnClickListener {
            startActivity(Intent(this@MainActivity, ActivityLawyerListBinding::class.java))
            finish()
        }

        binding.ibMainLawyer.setOnClickListener {
            startActivity(Intent(this@MainActivity, ActivityLawyerListBinding::class.java))
        }
        binding.ibMainLawWord.setOnClickListener {
            startActivity(Intent(this@MainActivity, ActivityLawWordBinding::class.java))
        }
        binding.ibMainPost.setOnClickListener {
        }
    }

    //TODO: recycler
    private fun setupRecycler() {
        binding.rvChatList.layoutManager = LinearLayoutManager(this)
        binding.rvChatList.adapter = RecyclerChatRoomAdapter(this)
    }
}