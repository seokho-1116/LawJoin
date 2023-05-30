package com.example.lawjoin.chat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.lawjoin.R
import com.example.lawjoin.data.model.ChatRoom
import com.example.lawjoin.data.model.Message
import com.example.lawjoin.data.model.User
import com.example.lawjoin.data.repository.ChatRoomRepository
import com.example.lawjoin.databinding.ActivityChatBinding
import com.example.lawjoin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.io.Serializable
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.O)
class ChatRoomActivity : AppCompatActivity() {
    private val chatRoomRepository = ChatRoomRepository.getInstance()
    private lateinit var myUid: String
    private lateinit var database: FirebaseDatabase

    lateinit var binding: ActivityChatBinding
    lateinit var auth: FirebaseAuth
    lateinit var chatRoom: ChatRoom
    private lateinit var receiver: User
    lateinit var chatRoomKey: String
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeProperty()
        initializeListener()
        setupChatRoom()
    }

    private fun initializeProperty() {
        auth = Firebase.auth
        myUid = auth.currentUser?.uid.toString()
        val intent = intent
        chatRoom = intent.serializable("ChatRoom")!!
        chatRoomKey = intent.serializable("ChatRoomKey")!!
        receiver = intent.serializable("Opponent")!!
        recyclerView = binding.rvChatContent
    }

    private fun initializeListener() {
        binding.ivChatBack.setOnClickListener() {
            startActivity(Intent(this@ChatRoomActivity, ActivityMainBinding::class.java))
        }
        binding.ibChatSend.setOnClickListener() {
            putMessage()
        }
    }

    private fun setupChatRoom() {
        setupProfile()
        if (chatRoomKey.isBlank())
            setupChatRoomKey()
        else
            setupRecycler()
    }

    private fun setupProfile() {
        val user = auth.currentUser
        Glide.with(this)
            .load(user?.photoUrl)
            .placeholder(R.drawable.ic_chat_user_default_profile)
            .error(R.drawable.ic_chat_user_default_profile)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivChatProfile)

        binding.tvReceiverDate.text = ZonedDateTime.now(TimeZone.getDefault().toZoneId())
            .toLocalDate().toString()
        binding.tvChatReceiver.text = receiver.name
    }

    private fun setupChatRoomKey() {
        chatRoomRepository.findUserChatRoomsByKey(myUid) {
            val chatRoom = it.children.first { chatRoom ->
                chatRoom.child("users").children.any { reference ->
                    reference.value as String == receiver.uid
                }
            }

            this.chatRoomKey = chatRoom.key.toString()
        }
    }

    private fun putMessage() {
        val message = Message(myUid, ZonedDateTime.now(ZoneId.of("UTC")), binding.edtChatInput.toString())
        chatRoomRepository.saveChatRoomMessage(chatRoomKey, message) {
            binding.edtChatInput.text.clear()
        }
    }

    private fun setupRecycler() {
        binding.rvChatContent.layoutManager = LinearLayoutManager(this)
        binding.rvChatContent.adapter = RecyclerChatAdapter(this, chatRoomKey)
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }
}