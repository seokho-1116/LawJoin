package com.example.lawjoin.word

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.lawjoin.data.model.LawWord
import com.example.lawjoin.data.repository.LawWordRepository
import com.example.lawjoin.databinding.ActivityLawWordBinding

class LawWordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLawWordBinding
    private lateinit var currentWord: LawWord
    private var lawWordRepository: LawWordRepository = LawWordRepository()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLawWordBinding.inflate(layoutInflater)

        val word = intent.getStringExtra("word")!!
        lawWordRepository.searchLawWord(word) {lawWord ->
            currentWord = lawWord
            binding.tvLawWord.text = lawWord.word
            binding.tvLawWordCommentary.text = tab.plus(lawWord.wordDescription)

            binding.btnLawWordNext.setOnClickListener {
                lawWordRepository.nextWord(currentWord.wordNumber.toInt()) {
                    currentWord = it
                    binding.tvLawWord.text = currentWord.word
                    binding.tvLawWordCommentary.text = tab.plus(currentWord.wordDescription)
                }
            }
            binding.btnLawWordPrevious.setOnClickListener {
                lawWordRepository.previousWord(currentWord.wordNumber.toInt()) {
                    currentWord = it
                    binding.tvLawWord.text = currentWord.word
                    binding.tvLawWordCommentary.text = tab.plus(currentWord.wordDescription)
                }
            }

            setContentView(binding.root)
        }

        binding.ibLawWordBack.setOnClickListener {
            startActivity(Intent(this, LawWordListActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        private const val tab = "\t\t\t\t\t\t\t\t"
    }
}