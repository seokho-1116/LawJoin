package com.example.lawjoin.login

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.lawjoin.MainActivity
import com.example.lawjoin.data.model.User
import com.example.lawjoin.data.repository.UserRepository
import com.example.lawjoin.databinding.ActivitySignUpBinding
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
class SignUpActivity : AppCompatActivity() {
    private val userRepository: UserRepository = UserRepository.getInstance()
    lateinit var auth: FirebaseAuth
    lateinit var btn_signUp: Button
    lateinit var edt_email: EditText
    lateinit var edt_password: EditText
    lateinit var edt_name: EditText

    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeView()
        initializeListener()
    }

    fun initializeView() {
        auth = FirebaseAuth.getInstance()
        btn_signUp = binding.btnStartSignUp
        edt_email = binding.edtSignUpEmail
        edt_password = binding.edtSignUpPassword
        edt_name = binding.edtSignUpName
    }

    fun initializeListener() {
        btn_signUp.setOnClickListener() {
            signUp()
        }
    }

    fun signUp() {     //회원 가입 실행
        var email = edt_email.text.toString()
        var password = edt_password.text.toString()
        var name = edt_name.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    try {
                        val user = auth.currentUser!!
                        val currentUser = User(
                            user.uid, user.email, name, ""
                        )
                        userRepository.saveUser(user.uid) {
                            it.setValue(currentUser)
                        }
                        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "화면 이동 중 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else
                    Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()

            }

    }
}