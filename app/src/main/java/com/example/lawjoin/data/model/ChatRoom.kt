package com.example.lawjoin.data.model

import java.io.Serializable

data class ChatRoom(val users: List<String>, var messages: MutableList<Message>) : Serializable