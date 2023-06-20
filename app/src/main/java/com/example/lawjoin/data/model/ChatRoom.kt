package com.example.lawjoin.data.model

import java.io.Serializable

data class ChatRoom(val messages: Map<String, Message> = mapOf(),
                    val users: List<String> = listOf()
) : Serializable