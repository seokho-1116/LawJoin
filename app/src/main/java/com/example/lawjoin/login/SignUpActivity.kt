package com.example.lawjoin.login

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lawjoin.MainActivity
import com.example.lawjoin.data.model.User
import com.example.lawjoin.data.repository.UserRepository
import com.example.lawjoin.databinding.ActivitySignUpBinding
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lawjoin.data.model.ChatRoom
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
class SignUpActivity : AppCompatActivity() {
    private val userRepository: UserRepository = UserRepository.getInstance()
    lateinit var auth: FirebaseAuth
    private lateinit var btn_signUp: Button
    private lateinit var edt_email: EditText
    private lateinit var edt_password: EditText
    private lateinit var edt_name: EditText

    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        initializeListener()
    }

    private fun initializeView() {
        auth = FirebaseAuth.getInstance()
        btn_signUp = binding.btnStartSignUp
        edt_email = binding.edtSignUpEmail
        edt_password = binding.edtSignUpPassword
        edt_name = binding.edtSignUpName
    }

    private fun initializeListener() {
        btn_signUp.setOnClickListener() {
            signUp()
        }
    }

    private fun signUp() {     //회원 가입 실행
        val email = edt_email.text.toString()
        val password = edt_password.text.toString()
        val name = edt_name.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser!!
                    val currentUser = User(
                        user.uid, user.email, name, ""
                    )
                    userRepository.saveUser(user.uid) {
                        it.setValue(currentUser)
                    }
                    Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    intent.putExtra("isSignUp", true)
                    startActivity(intent)
                } else
                Toast.makeText(this, "이메일은 형식에 맞추어야 하고 비밀번호는 6자리 이상이여야합니다.", Toast.LENGTH_SHORT).show()
            }
    }
}