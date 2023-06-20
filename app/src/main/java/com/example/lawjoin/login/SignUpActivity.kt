package com.example.lawjoin.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.lawjoin.data.model.User
import com.example.lawjoin.data.repository.UserRepository
import com.example.lawjoin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

@RequiresApi(Build.VERSION_CODES.O)
class SignUpActivity : AppCompatActivity() {
    private val userRepository: UserRepository = UserRepository.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var name: String

    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeListener()
    }

    private fun initializeListener() {
        binding.btnStartSignUp.setOnClickListener() {
            validate()
        }
    }

    private fun validate() {
        email = binding.edtSignUpEmail.text.toString()
        password = binding.edtSignUpPassword.text.toString()
        name = binding.edtSignUpName.text.toString()

        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            Toast.makeText(this, "모든 값이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "비밀번호가 6자리보다 짧습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val pattern: Pattern = Patterns.EMAIL_ADDRESS

        if (!pattern.matcher(email).matches()) {
            Toast.makeText(this, "이메일이 형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!binding.cbAgree.isChecked) {
            Toast.makeText(this, "개인정보 처리 방침에 동의해 주셔야 합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        signUp()
    }

    private fun signUp() {

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
                    finish()
                } else
                    Toast.makeText(this, "이메일은 형식에 맞추어야 하고 비밀번호는 6자리 이상이여야 합니다.", Toast.LENGTH_SHORT).show()
            }
    }
}