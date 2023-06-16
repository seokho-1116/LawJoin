package com.example.lawjoin

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.lawjoin.chat.ChatRoomActivity
import com.example.lawjoin.common.AuthUtils
import com.example.lawjoin.data.model.AuthUserDto
import com.example.lawjoin.data.model.ChatRoom
import com.example.lawjoin.data.model.LawyerDto
import com.example.lawjoin.data.repository.ChatRoomRepository
import com.example.lawjoin.data.repository.LawyerRepository
import com.example.lawjoin.databinding.ChatRoomItemBinding
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
class RecyclerChatRoomAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerChatRoomAdapter.ViewHolder>(), Filterable {
    private val storage = FirebaseStorage.getInstance()
    private val formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    private val lawyerRepository: LawyerRepository = LawyerRepository.getInstance()
    private val chatRoomRepository: ChatRoomRepository = ChatRoomRepository.getInstance()
    private var chatRooms: MutableList<ChatRoom> = mutableListOf()
    private var excelSearchList: List<ChatRoom> = chatRooms
    private var chatRoomKeys: MutableList<String> = mutableListOf()
    private lateinit var currentUser: AuthUserDto

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        AuthUtils.getCurrentUser { authUserDto, _ ->
            currentUser = authUserDto!!
            setupAllChatRoomList()
        }
    }

    private fun setupAllChatRoomList() {
        chatRoomRepository.findAllChatRoomsByUid(currentUser.uid!!) {
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

        val opponent = excelSearchList[position].users.first { it != currentUser.uid }
        lawyerRepository.findLawyerById(opponent) {
            holder.chatProfile = it.profile_url
            holder.opponentUser = LawyerDto(it.uid, it.name, it.email)
            holder.receiver.text = it.name

            when (opponent) {
                "GPT" -> {
                    retrieveProfileUrl("profile/GPT.png") { url ->
                        setProfileAndConfigureScreen(holder, url)
                    }
                }
                "BOT" -> {
                    retrieveProfileUrl("profile/BOT.png") { url ->
                        setProfileAndConfigureScreen(holder, url)
                    }
                }
                else -> {
                    setProfileAndConfigureScreen(holder,holder.chatProfile)
                }
            }

            if (excelSearchList[position].messages.isNotEmpty()) {
                setupLastMessageAndDate(holder, position)
            }
            setupMessageCount(holder, position)
        }

        holder.layout.setOnClickListener {
            val intent = Intent(context, ChatRoomActivity::class.java)
            intent.putExtra("chat_room", chatRooms[position])
            intent.putExtra("receiver", holder.opponentUser)
            intent.putExtra("chat_room_key", chatRoomKeys[position])
            context.startActivity(intent)
        }
    }

    private fun retrieveProfileUrl(path: String, callback: (String) -> Unit) {
        storage.reference.child(path)
            .downloadUrl.addOnSuccessListener { uri ->
                val url = uri.toString()
                callback(url)
            }
    }

    private fun setProfileAndConfigureScreen(holder: ViewHolder, url: String) {
        holder.chatProfile = url

        Glide.with(context)
            .load(holder.chatProfile)
            .error(R.drawable.ic_lawyer_basic)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.profileImage)
    }


    private fun setupLastMessageAndDate(holder: ViewHolder, position: Int) {
        val lastMessage = excelSearchList[position].messages.values
            .sortedWith(compareBy { it.sendDate })
            .last()
        holder.lastMessage.text = lastMessage.content
        holder.date.text = getLastMessageTimeString(ZonedDateTime.parse(lastMessage.sendDate, formatter))
    }

    private fun setupMessageCount(holder: ViewHolder, position: Int) {
        val unconfirmedCount =
            excelSearchList[position].messages.values.filter {
                !it.confirmed && it.senderUid != currentUser.uid
            }.size

        if (unconfirmedCount > 0) {
            holder.unreadCount.visibility = View.VISIBLE
            holder.unreadCount.text = unconfirmedCount.toString()
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
        return excelSearchList.size
    }

    override fun getFilter() : Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                excelSearchList = if (charString.isEmpty()){
                    chatRooms
                } else {
                    val filteredList = mutableListOf<ChatRoom>()

                    //TODO: 반대편 사용자 리스트 가져왔고 반대편 사용자 이름과 사용자가 입력한 이름을 필터링
                    for (chatRoom in chatRooms) {
                        val opponent = chatRoom.users.first {
                            it != currentUser.uid
                        }.toString()
                        LawyerRepository.getInstance().findLawyerById(opponent) {
                            val name = it.name
                            if (name.toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(chatRoom)
                            }
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = excelSearchList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                excelSearchList = filterResults.values as List<ChatRoom>
                notifyDataSetChanged()
            }
        }
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
        var profileImage = binding.ivChatProfile
    }
}