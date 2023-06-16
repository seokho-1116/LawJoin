package com.example.lawjoin.start

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.lawjoin.databinding.ActivityStartInfoBinding
import com.example.lawjoin.login.LoginActivity
import com.example.lawjoin.start.fragment.FirstInfoFragment
import com.example.lawjoin.start.fragment.SecondInfoFragment
import com.example.lawjoin.start.fragment.ThirdInfoFragment

@RequiresApi(Build.VERSION_CODES.O)
class StartInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPref.getBoolean("isFirstRun", true)

        if (!isFirstRun) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val editor = sharedPref.edit()
        editor.putBoolean("isFirstRun", false)
        editor.apply()

        binding = ActivityStartInfoBinding.inflate(layoutInflater)

        val adapter = StartInfoFragmentPagerAdapter(this)
        adapter.addFragment(FirstInfoFragment())
        adapter.addFragment(SecondInfoFragment())
        adapter.addFragment(ThirdInfoFragment())
        binding.vpStart.adapter = adapter

        setContentView(binding.root)
    }
}