package com.example.lawjoin

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.chat.ChatRoomActivity
import com.example.lawjoin.data.model.ChatRoom
import com.example.lawjoin.data.repository.ChatRoomRepository
import com.example.lawjoin.data.repository.LawyerRepository
import com.example.lawjoin.databinding.ChatRoomItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.TimeZone


@RequiresApi(Build.VERSION_CODES.O)
class RecyclerChatRoomAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerChatRoomAdapter.ViewHolder>() {
    private val lawyerRepository: LawyerRepository = LawyerRepository.getInstance()
    private val chatRoomRepository: ChatRoomRepository = ChatRoomRepository.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var chatRooms: MutableList<ChatRoom> = mutableListOf()
    var chatRoomKeys: MutableList<String> = mutableListOf()
    private var myUid: String
    private var auth: FirebaseAuth = Firebase.auth

    init {
        myUid = auth.currentUser?.uid.toString()
        setupAllChatRoomList()
    }

    private fun setupAllChatRoomList() {
        chatRoomRepository.findAllChatRoomsByUid(myUid) {
            chatRooms.clear()
            chatRooms.add(it!!.getValue<ChatRoom>()!!)
            chatRoomKeys.add(it.key.toString())

            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_room_item, parent, false)
        return ViewHolder(ChatRoomItemBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val opponent = chatRooms[position].users.first { it != myUid }
        lawyerRepository.findLawyerById(opponent) {
            holder.chatProfile = it.profile_url
            holder.opponentUser = it.uid!!
            holder.receiver.text = it.name
        }

        holder.layout.setOnClickListener() {
            val intent = Intent(context, ChatRoomActivity::class.java)
            intent.putExtra("chat_room", chatRooms[position])
            intent.putExtra("receiver", holder.opponentUser)
            intent.putExtra("chat_room_key", chatRoomKeys[position])
            context.startActivity(intent)
            (context as AppCompatActivity).finish()
        }

        if (chatRooms[position].messages!!.isNotEmpty()) {
            setupLastMessageAndDate(holder, position)
            setupMessageCount(holder, position)
        }
    }

    private fun setupLastMessageAndDate(holder: ViewHolder, position: Int) {
        val lastMessage = chatRooms[position].messages
            .sortedWith(compareBy { it.sendDate })
            .last()
        holder.lastMessage.text = lastMessage.content
        holder.date.text = getLastMessageTimeString(lastMessage.sendDate)
    }

    private fun setupMessageCount(holder: ViewHolder, position: Int) {
        val unconfirmedCount =
            chatRooms[position].messages.filter {
                it.confirmed && it.senderUid != myUid
            }.size

        if (unconfirmedCount > 0) {
            holder.unreadCount.visibility = View.VISIBLE
            holder.unreadCount.text = unconfirmedCount.toString()
        } else {
            holder.unreadCount.visibility = View.GONE
        }
    }

    private fun getLastMessageTimeString(lastTimeString: ZonedDateTime): String {
        val zoneId = TimeZone.getDefault().toZoneId()
        val withZoneSameLocal = lastTimeString.withZoneSameLocal(zoneId)
        val currentTime = LocalDateTime.now().atZone(zoneId)

        val monthAgo = currentTime.monthValue - withZoneSameLocal.monthValue
        val dayAgo = currentTime.dayOfMonth - withZoneSameLocal.dayOfMonth
        val hourAgo = currentTime.hour - withZoneSameLocal.hour
        val minuteAgo = currentTime.minute - withZoneSameLocal.minute

        return when {
            monthAgo > 0 -> monthAgo.toString() + "개월 전"
            dayAgo > 1 -> dayAgo.toString() + "일 전"
            dayAgo == 0 -> "어제"
            hourAgo > 0 -> hourAgo.toString() + "시간 전"
            minuteAgo > 0 -> minuteAgo.toString() + "분 전"
            else -> "방금"
        }
    }

    override fun getItemCount(): Int {
        return chatRooms.size
    }

    inner class ViewHolder(itemView: ChatRoomItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var chatProfile = ""
        var opponentUser = ""
        var chatRoomKey = ""
        var layout = itemView.root
        var receiver = itemView.tvChatRoomReceiver
        var lastMessage = itemView.tvChatRoomText
        var date = itemView.tvChatDate
        var unreadCount = itemView.tvChatRoomNotificationCount
    }

}