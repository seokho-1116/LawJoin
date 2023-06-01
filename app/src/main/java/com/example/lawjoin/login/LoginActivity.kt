package com.example.lawjoin.login

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.lawjoin.MainActivity
import com.example.lawjoin.R
import com.example.lawjoin.data.model.ChatRoom
import com.example.lawjoin.data.model.User
import com.example.lawjoin.data.repository.ChatRoomRepository
import com.example.lawjoin.data.repository.UserRepository
import com.example.lawjoin.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

@RequiresApi(Build.VERSION_CODES.O)
class LoginActivity: Activity() {
    private var chatRoomRepository = ChatRoomRepository.getInstance()
    private var userRepository = UserRepository.getInstance()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idToken = Firebase.auth.currentUser?.getIdToken(true)
        val user = Firebase.auth.currentUser
        if (user != null && idToken != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.btnSignIn.setOnClickListener {
            signIn()
        }

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d(TAG, "signInWithCredential:success")
                    userRepository.saveUser(user!!.uid) {
                        it.setValue(User(
                            user.uid, user.email, user.displayName,
                            user.phoneNumber, user.photoUrl.toString()
                        ))
                    }
                    addChatRoom()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun addChatRoom() {
        val uid  = auth.currentUser!!.uid
        val chatRoomBot = ChatRoom(mapOf(), listOf(uid,"BOT"))
        val chatRoomGpt = ChatRoom(mapOf(), listOf(uid,"GPT"))

        chatRoomRepository.findUserInitChatRoom(uid, "BOT", "GPT") {
            if (!it.exists()) {
                chatRoomRepository.saveChatRoomUnder(uid) { reference ->
                    reference.child("BOT")
                        .setValue(chatRoomBot)

                    reference.child("GPT")
                        .setValue(chatRoomGpt)
                }
            }
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}