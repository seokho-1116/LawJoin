package com.example.lawjoin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.lawjoin.account.AccountManagementActivity
import com.example.lawjoin.common.AuthUtils
import com.example.lawjoin.data.model.AuthUserDto
import com.example.lawjoin.databinding.ActivityMainBinding
import com.example.lawjoin.lawyer.LawyerListActivity
import com.example.lawjoin.post.PostDetailActivity
import com.example.lawjoin.word.LawWordListActivity
import com.google.android.material.navigation.NavigationView

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentUser: AuthUserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        initializeView()
        initializeListener()
        setupRecycler()
        setContentView(binding.root)
    }

    private fun initializeView() {
        AuthUtils.getCurrentUser { authUserDto, _ ->
            currentUser = authUserDto!!

            Glide.with(this)
                .load(currentUser.chatProfile)
                .circleCrop()
                .into(binding.btnChatProfile)
        }

        binding.ibMainMessage.isClickable = false
        binding.ibMainMessage.isSelected = true
    }

    private fun initializeListener() {
        binding.btnChatProfile.setOnClickListener {
            startActivity(Intent(this, AccountManagementActivity::class.java))
        }
        binding.btnStartChat.setOnClickListener {
            startActivity(Intent(this, LawyerListActivity::class.java))
            finish()
        }
        binding.ibMainLawyer.setOnClickListener {
            startActivity(Intent(this, LawyerListActivity::class.java))
        }
        binding.ibMainLawWord.setOnClickListener {
            startActivity(Intent(this, LawWordListActivity::class.java))
        }
        binding.ibMainPost.setOnClickListener {
            startActivity(Intent(this, PostDetailActivity::class.java))
        }
    }

    private fun setupRecycler() {
        binding.rvChatList.layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerChatRoomAdapter(this)
        binding.rvChatList.adapter = adapter

        val searchViewTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(s: String): Boolean {
                    adapter.filter.filter(s)
                    return false
                }
            }

        binding.edtChatSearch.setOnQueryTextListener(searchViewTextListener)
    }
}