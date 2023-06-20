package com.example.lawjoin.common

import android.util.Log
import com.example.lawjoin.data.model.AuthUserDto
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.user.UserApiClient

object AuthUtils {
    fun getCurrentUser(callback: (AuthUserDto?, Throwable?) -> Unit) {
        val googleUser = Firebase.auth.currentUser
        val currentUser = AuthUserDto(
            googleUser?.uid,
            googleUser?.displayName,
            googleUser?.email,
            googleUser?.photoUrl.toString()
        )

        if (googleUser == null) {
            UserApiClient.instance.me { kakaoUser, error ->
                if (error != null) {
                    Log.e("RecyclerChatAdapter", "사용자 정보 요청 실패", error)
                    callback(null, error)
                } else if (kakaoUser != null) {
                    val authUserDto = AuthUserDto(
                        kakaoUser.id.toString(),
                        kakaoUser.kakaoAccount?.name,
                        kakaoUser.kakaoAccount?.email,
                        kakaoUser.kakaoAccount?.profile?.thumbnailImageUrl.toString()
                    )
                    callback(authUserDto, null)
                }
            }
        } else {
            callback(currentUser, null)
        }
    }
}