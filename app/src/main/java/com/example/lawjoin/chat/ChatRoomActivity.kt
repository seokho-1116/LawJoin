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
import com.example.lawjoin.MainActivity
import com.example.lawjoin.R
import com.example.lawjoin.data.model.ChatRoom
import com.example.lawjoin.data.model.LawyerDto
import com.example.lawjoin.data.model.Message
import com.example.lawjoin.data.model.User
import com.example.lawjoin.data.repository.ChatRoomRepository
import com.example.lawjoin.databinding.ActivityChatBinding
import com.example.lawjoin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.io.Serializable
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.O)
class ChatRoomActivity : AppCompatActivity() {
    private val chatRoomRepository = ChatRoomRepository.getInstance()
    private lateinit var myUid: String
    private lateinit var binding: ActivityChatBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var chatRoom: ChatRoom
    private lateinit var receiver: LawyerDto
    private lateinit var chatRoomKey: String
    private lateinit var chatAdapter: RecyclerChatAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeProperties()
        initializeListeners()
        setupChatRoom()
    }

    private fun initializeProperties() {
        auth = Firebase.auth
        myUid = auth.currentUser?.uid.orEmpty()
        val intent = intent
        chatRoom = intent.serializable("chat_room") ?: return
        chatRoomKey = intent.serializable<String>("chat_room_key").orEmpty()
        receiver = intent.serializable("receiver") ?: return
        recyclerView = binding.rvChatContent
        chatAdapter = RecyclerChatAdapter(this, chatRoomKey)
    }

    private fun initializeListeners() {
        binding.ivChatBack.setOnClickListener {
            startActivity(Intent(this@ChatRoomActivity, MainActivity::class.java))
        }
        binding.ibChatSend.setOnClickListener {
            putMessage()
        }
    }

    private fun setupChatRoom() {
        setupProfile()
        setupRecycler()
    }

    private fun setupProfile() {
        val user = auth.currentUser
        Glide.with(this)
            .load(user?.photoUrl)
            .placeholder(R.drawable.ic_chat_user_default_profile)
            .error(R.drawable.ic_chat_user_default_profile)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.btnChatProfile)

        binding.tvReceiverDate.text = ZonedDateTime.now(TimeZone.getDefault().toZoneId())
            .toLocalDate().toString()
        binding.tvChatReceiver.text = receiver.name
    }

    private fun putMessage() {
        if (receiver.uid == "GPT") {
            //sendToGPT()
        } else if (receiver.uid == "BOT") {
            //sendToBot()
        }

        val message = Message(
            myUid,
            ZonedDateTime.now(ZoneId.of("UTC")).toString(),
            binding.edtChatInput.text.toString()
        )
        chatRoomRepository.saveChatRoomMessage(chatRoomKey, message) {
            binding.edtChatInput.text.clear()
        }
    }

    private fun setupRecycler() {
        binding.rvChatContent.layoutManager = LinearLayoutManager(this)
        binding.rvChatContent.adapter = chatAdapter
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }
}
