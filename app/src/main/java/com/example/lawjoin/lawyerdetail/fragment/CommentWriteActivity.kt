package com.example.lawjoin.lawyerdetail.fragment

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.lawjoin.common.AuthUtils
import com.example.lawjoin.data.model.AuthUserDto
import com.example.lawjoin.data.model.CounselReview
import com.example.lawjoin.data.repository.LawyerRepository
import com.example.lawjoin.databinding.ActivityCommentWriteBinding
import java.time.ZoneId
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class CommentWriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentWriteBinding
    private lateinit var currentUser: AuthUserDto
    private val lawyerRepository = LawyerRepository.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCommentWriteBinding.inflate(layoutInflater)

        val lawyerId = intent.getStringExtra("lawyerId")!!
        AuthUtils.getCurrentUser { authUserDto, _ ->
            if (authUserDto != null) {
                currentUser = authUserDto
            }
        }

        binding.btnWrite.setOnClickListener {
            val counselReview = CounselReview(
                binding.edtCommentTitle.text.toString(), binding.edtCommentContent.text.toString(),
                ZonedDateTime.now(ZoneId.of("UTC")).toString(), currentUser.name!!,
                binding.edtCommentCategory.text.toString(), lawyerId
            )
            lawyerRepository.saveLawyerReview(lawyerId, counselReview)
            finish()
        }

        setContentView(binding.root)
    }
}