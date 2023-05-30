package com.example.lawjoin.chat

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.R
import com.example.lawjoin.data.model.Message
import com.example.lawjoin.data.repository.ChatRoomRepository
import com.example.lawjoin.databinding.ChatMessageReceiverBinding
import com.example.lawjoin.databinding.ChatMessageSenderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.ZonedDateTime
import kotlin.text.StringBuilder

@RequiresApi(Build.VERSION_CODES.O)
class RecyclerChatAdapter(
    private val context: Context,
    var chatRoomKey: String,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val chatRoomRepository = ChatRoomRepository.getInstance()
    var messages: MutableList<Message> = mutableListOf()
    var messageKeys: MutableList<String> = mutableListOf()
    private val auth: FirebaseAuth = Firebase.auth
    private val myUid = auth.currentUser?.uid.toString()
    private val recyclerView = (context as ChatRoomActivity).recyclerView

    init {
        setupMessages()
    }

    private fun setupMessages() {
        getMessages()
    }

    private fun getMessages() {
        chatRoomRepository.findUserChatRoomByKey(myUid, chatRoomKey) {
            messages.clear()
            it.child("messages").children.forEach { ref ->
                messages.add(ref.value as Message)
                messageKeys.add(ref.key.toString())
            }
            notifyDataSetChanged()
            recyclerView.scrollToPosition(messages.size - 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderUid == myUid) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.chat_message_sender, parent, false)

                ChatMessageSenderViewHolder(ChatMessageSenderBinding.bind(view))
            }

            else -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.chat_message_receiver, parent, false)
                ChatMessageReceiverViewHolder(ChatMessageReceiverBinding.bind(view))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (messages[position].senderUid == myUid) {
            (holder as ChatMessageSenderViewHolder).bind(position)
        } else {
            (holder as ChatMessageReceiverViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class ChatMessageReceiverViewHolder(itemView: ChatMessageReceiverBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private var message = itemView.tvReceiverText
        private var date = itemView.tvReceiverDate
        private var isShown = itemView.tvReceiverIsRead

        fun bind(position: Int) {
            message.text = messages[position].content

            date.text = getDateText(messages[position].sendDate)

            if (messages[position].confirmed)
                isShown.visibility = View.GONE
            else
                isShown.visibility = View.VISIBLE

            setShown(position)
        }
    }

    inner class ChatMessageSenderViewHolder(itemView: ChatMessageSenderBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private var txtMessage = itemView.tvSenderText
        private var txtDate = itemView.tvSenderDate
        private var isShown = itemView.tvSenderIsRead

        fun bind(position: Int) {
            txtMessage.text = messages[position].content

            txtDate.text = getDateText(messages[position].sendDate)

            if (messages[position].confirmed)
                isShown.visibility = View.GONE
            else
                isShown.visibility = View.VISIBLE
        }

    }

    private fun getDateText(sendDate: ZonedDateTime): String {    //메시지 전송 시각 생성

        val dateText = StringBuilder()
        val timeFormat = "%02d:%02d"

        val hour = sendDate.hour
        val minute = sendDate.minute
        if (hour > 11) {
            dateText.append("오후 ")
            dateText.append(timeFormat.format(hour - 12, hour))
        } else {
            dateText.append("오전 ")
            dateText.append(timeFormat.format(hour, minute))
        }
        return dateText.toString()
    }

    private fun setShown(position: Int) {
        chatRoomRepository.updateMessageConfirm(myUid, chatRoomKey, messageKeys[position])
    }
}
