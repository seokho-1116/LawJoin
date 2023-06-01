package com.example.lawjoin.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lawjoin.MainActivity
import com.example.lawjoin.R
import com.example.lawjoin.data.objects.MenuObjects

class AccountManagementActivity : AppCompatActivity() {
    var context : Context = this
    lateinit var menuAdapter : MenuAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_management)

        val menuObjects = ArrayList<MenuObjects>()
        val reservationCancelBtn = findViewById<Button>(R.id.reservationCancelBtn)
        val reservationChangeBtn = findViewById<Button>(R.id.reservationChangeBtn)
        val reservationDate = findViewById<TextView>(R.id.reservationDate)
        val mainBtn = findViewById<Button>(R.id.mainBtn)
        val rcMenuList = findViewById<RecyclerView> (R.id.menu_rcView)
        menuAdapter = MenuAdapter(menuObjects, this)

        menuObjects.add(MenuObjects("좋아요한 변호사", 1))
        menuObjects.add(MenuObjects("북마크한 법률 단어 보기", 2))
        menuObjects.add(MenuObjects("내가 쓴 글", 3))
        menuObjects.add(MenuObjects("내 정보", 4))
        menuObjects.add(MenuObjects("앱 설정", 5))
        menuObjects.add(MenuObjects("로그아웃", 6))

        val menuLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        rcMenuList.layoutManager = menuLayoutManager
        rcMenuList.adapter =  menuAdapter

        mainBtn.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        reservationCancelBtn.setOnClickListener(){

        }
        /*reservationChangeBtn.setOnClickListener(){
            val intent = Intent(context, ).apply{
                putExtra("data", )
            }.run{context.startActivity(this)}
            startActivity(intent)
        }*/
    }
}