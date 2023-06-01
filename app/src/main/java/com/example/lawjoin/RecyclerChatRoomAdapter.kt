package com.example.lawjoin

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.chat.ChatRoomActivity
import com.example.lawjoin.data.model.ChatRoom
import com.example.lawjoin.data.model.LawyerDto
import com.example.lawjoin.data.repository.ChatRoomRepository
import com.example.lawjoin.data.repository.LawyerRepository
import com.example.lawjoin.databinding.ChatRoomItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
class RecyclerChatRoomAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerChatRoomAdapter.ViewHolder>() {
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    private val lawyerRepository: LawyerRepository = LawyerRepository.getInstance()
    private val chatRoomRepository: ChatRoomRepository = ChatRoomRepository.getInstance()
    private var chatRooms: MutableList<ChatRoom> = mutableListOf()
    private var chatRoomKeys: MutableList<String> = mutableListOf()
    private var myUid: String
    private var auth: FirebaseAuth = Firebase.auth

    init {
        myUid = auth.currentUser?.uid.toString()
        setupAllChatRoomList()
    }

    private fun setupAllChatRoomList() {
        chatRoomRepository.findAllChatRoomsByUid(myUid) {
            chatRooms.clear()
            for (data in it.children) {
                chatRooms.add(data.getValue(ChatRoom::class.java)!!)
                chatRoomKeys.add(data.key.toString())
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChatRoomItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val opponent = chatRooms[position].users.first { it != myUid }
        lawyerRepository.findLawyerById(opponent) {
            holder.chatProfile = it.profile_url
            holder.opponentUser = LawyerDto(it.uid, it.name, it.email)
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

        if (chatRooms[position].messages.isNotEmpty()) {
            setupLastMessageAndDate(holder, position)
        }
        setupMessageCount(holder, position)
    }

    private fun setupLastMessageAndDate(holder: ViewHolder, position: Int) {
        val lastMessage = chatRooms[position].messages.values
            .sortedWith(compareBy { it.sendDate })
            .last()
        holder.lastMessage.text = lastMessage.content
        holder.date.text = getLastMessageTimeString(ZonedDateTime.parse(lastMessage.sendDate, formatter))
    }

    private fun setupMessageCount(holder: ViewHolder, position: Int) {
        val unconfirmedCount =
            chatRooms[position].messages.values.filter {
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
        val zoneId = ZoneId.systemDefault()
        val messageTime = lastTimeString.withZoneSameInstant(zoneId)
        val currentTime = LocalDateTime.now().atZone(zoneId)

        val difference = ChronoUnit.MINUTES.between(messageTime, currentTime)
        return when {
            difference < 1 -> "방금 전"
            difference < 60 -> "${difference}분 전"
            difference < 1440 -> "${difference / 60}시간 전"
            difference < 43200 -> "${difference / 1440}일 전"
            else -> "${difference / 43200}달 전"
        }
    }

    override fun getItemCount(): Int {
        return chatRooms.size
    }

    class ViewHolder(binding: ChatRoomItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var chatProfile = ""
        var opponentUser = LawyerDto()
        var layout = binding.root
        var receiver = binding.tvChatRoomReceiver
        var lastMessage = binding.tvChatRoomText
        var date = binding.tvChatDate
        var unreadCount = binding.tvChatRoomNotificationCount
    }
}