package com.example.lawjoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lawjoin.data.repository.PostRepository

class MainActivity : AppCompatActivity() {
    val postRepository:PostRepository = PostRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}