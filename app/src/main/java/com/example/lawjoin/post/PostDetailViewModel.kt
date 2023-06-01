package com.example.lawjoin.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lawjoin.data.model.User
import com.example.lawjoin.data.repository.UserRepository

class PostDetailViewModel: ViewModel(){
    private val userRepository = UserRepository.getInstance()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun findUser(uid: String) {
        userRepository.findUser(uid) {
            _user.value = it.value as User
        }
    }
}
