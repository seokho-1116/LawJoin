package com.example.lawjoin.post

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.lawjoin.R

class WritePostActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_write)

        val btnWrite = findViewById<Button>(R.id.btn_write)
        val edit_post_title = findViewById<EditText>(R.id.edt_comment_title)
        val edit_post_content = findViewById<EditText>(R.id.edt_comment_content)
        val cb_write_anonymous = findViewById<CheckBox>(R.id.cb_write_anonymous)
        val cb_for_only_lawyer = findViewById<CheckBox>(R.id.cb_for_only_lawyer)

        // 제목 작성

        // 본문 작성


        // 글쓰기 버튼
        btnWrite.setOnClickListener {
            // 게시물 보여주는 창 이동
            // 익명 체크
            // 변호사만

            // db저장
        }



    }
}



