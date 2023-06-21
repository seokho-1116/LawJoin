package com.example.lawjoin.post

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lawjoin.data.model.Post
import com.example.lawjoin.data.model.User
import com.example.lawjoin.data.repository.UserRepository

class PostDetailViewModel: ViewModel(){
    private val userRepository = UserRepository.getInstance()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    @RequiresApi(Build.VERSION_CODES.O)
    fun findUser(uid: String, callback: (User?) -> Unit) {
        userRepository.findUser(uid) {
            _user.value = it.getValue(User::class.java)

            for (postSnapshot in it.child("bookmarked_posts").children) {
                val postId = postSnapshot.key
                _user.value?.bookmarkedPosts!!.add(postId ?: "")
            }

            for (postSnapshot in it.child("recommended_posts").children) {
                val postId = postSnapshot.key
                _user.value?.recommendPosts!!.add(postId ?: "")
            }

            callback(_user.value)
        }
    }

    fun updateUserBookmark(post: Post) {
        userRepository.updateBookmark(user.value?.uid!!, post)
    }

    fun updateUserRecommendPost(post: Post) {
        userRepository.updateRecommendedPost(user.value?.uid!!, post)
    }

    fun deleteBookmark(post: Post) {
        if (user.value?.bookmarkedPosts!!.contains(post.id)) {
            userRepository.deleteBookmark(user.value?.uid!!, post)
        }
    }

    fun deleteRecommend(post: Post) {
        if (user.value?.recommendPosts!!.contains(post.id)) {
            userRepository.deleteRecommend(user.value?.uid!!, post)
        }
    }
}
